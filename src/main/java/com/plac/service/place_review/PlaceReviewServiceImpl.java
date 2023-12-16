package com.plac.service.place_review;

import com.plac.domain.Place;
import com.plac.domain.User;
import com.plac.domain.place_review.*;
import com.plac.dto.request.place_review.PlaceReviewRateReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.dto.response.place_review.PlaceReviewResDto;
import com.plac.exception.place.WrongKakaoPlaceIdException;
import com.plac.exception.place.WrongPlaceIdException;
import com.plac.exception.place_review.CannotRateReviewException;
import com.plac.exception.place_review.PlaceReviewNotFoundException;
import com.plac.repository.*;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PlaceReviewServiceImpl implements PlaceReviewService{

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceRepository placeRepository;
    private final PlaceReviewTagRepository placeReviewTagRepository;
    private final PlaceReviewImageRepository placeReviewImageRepository;
    private final PlaceReviewTagMappingRepository placeReviewTagMappingRepository;
    private final PlaceReviewLikeRepository placeReviewLikeRepository;
    private final PlaceReviewDislikeRepository placeReviewDislikeRepository;
    private final UserRepository userRepository;

    // 리뷰 등록 시, 생성되는 테이블 : PlaceReview, PlaceReviewTag, PlaceReviewTagMapping, PlaceReviewImage
    @Override
    public void writeReview(PlaceReviewReqDto req) {
        Optional<Place> placeOpt = placeRepository.findByKakaoPlaceId(req.getKakaoPlaceId());
        if(!placeOpt.isPresent()){
            throw new WrongKakaoPlaceIdException("Wrong kakaoPlaceId!");
        }
        if(placeOpt.get().getId() != req.getPlaceId()){
            throw new WrongPlaceIdException("Wrong placeId");
        }

        PlaceReview placeReview = PlaceReview.builder()
                .placeId(req.getPlaceId())
                .ratings(req.getRatings())
                .content(req.getContent())
                .userId(SecurityContextHolderUtil.getUserId())
                .build();

        placeReviewRepository.save(placeReview);

        AtomicInteger turn = new AtomicInteger(0);

        if (req.getPictureUrl() != null){
            req.getPictureUrl().forEach(pictureUrl -> {
                    PlaceReviewImage placeReviewImage = PlaceReviewImage.builder()
                            .imageUrl(pictureUrl)
                            .placeReview(placeReview)
                            .turn(turn.getAndIncrement())
                            .build();
                    placeReviewImageRepository.save(placeReviewImage);
            });
        }

        /**
         * 성능 */
        if(req.getTags() != null) {
            req.getTags().forEach(tag -> {
                PlaceReviewTag placeReviewTag = placeReviewTagRepository.findByTagName(tag)
                        .orElseGet(() -> createPlaceReviewTag(tag));

                PlaceReviewTagMapping placeReviewTagMapping = PlaceReviewTagMapping.builder()
                        .placeReview(placeReview)
                        .placeReviewTag(placeReviewTag)
                        .build();
                placeReviewTagMappingRepository.save(placeReviewTagMapping);
            });
        }
    }

    @Override
    public PlaceReviewTag createPlaceReviewTag(String tagName){
        PlaceReviewTag placeReviewTag = PlaceReviewTag.builder()
                .tagName(tagName).build();
        return placeReviewTagRepository.save(placeReviewTag);
    }

    @Override
    public void addLikeToPlaceReview(PlaceReviewRateReqDto req) {
        PlaceReview placeReview = verifyPlaceReview(placeReviewRepository.findById(req.getPlaceReviewId()));

        PlaceReviewLike placeReviewLike = PlaceReviewLike.builder()
                .placeReview(placeReview)
                .userId(SecurityContextHolderUtil.getUserId()).build();
        placeReviewLikeRepository.save(placeReviewLike);
    }

    @Override
    public void addDisLikeToPlaceReview(PlaceReviewRateReqDto req) {
        PlaceReview placeReview = verifyPlaceReview(placeReviewRepository.findById(req.getPlaceReviewId()));

        PlaceReviewDislike placeReviewLike = PlaceReviewDislike.builder()
                .placeReview(placeReview)
                .userId(SecurityContextHolderUtil.getUserId()).build();
        placeReviewDislikeRepository.save(placeReviewLike);
    }

    @Override
    public List<PlaceReviewResDto> getPlaceReviews(Long placeId, String sortBy, int page) {
        System.out.println("service - getPlaceReviews()");
        List<PlaceReviewResDto> results = new ArrayList<>();

        if(sortBy.equals("최신순")){
            // 페이지 번호는 0부터 시작
            PageRequest pageRequest = PageRequest.of(page, 3); // 페이지 크기는 3
            Page<PlaceReview> result = placeReviewRepository.findAllOrderByCreatedAtDesc(placeId, pageRequest);
            List<PlaceReview> placeReviews = result.getContent();
            for (PlaceReview placeReview : placeReviews) {
                Long placeReviewId = placeReview.getId();
                User user = userRepository.findById(placeReview.getUserId()).get();
                String profileName = user.getProfileName();

                LocalDateTime createdAt = placeReview.getCreatedAt();
                Ratings ratings = placeReview.getRatings();
                String content = placeReview.getContent();
                int likeCount = placeReviewLikeRepository.countByPlaceReviewId(placeReviewId);
                int disLikeCount = placeReviewDislikeRepository.countByPlaceReviewId(placeReviewId);

                boolean myReview, pickLike, pickDisLike;

                // 해당 리뷰를 쓴 사람과 현재 로그인된 사람이 같다.
                if(placeReview.getUserId() == SecurityContextHolderUtil.getUserId()){
                    myReview = true;
                }else myReview = false;

                Optional<PlaceReviewLike> placeReviewLikeOpt = placeReviewLikeRepository.findByPlaceReviewIdAndUserId(placeReviewId, SecurityContextHolderUtil.getUserId());
                Optional<PlaceReviewLike> placeReviewDislikeOpt = placeReviewDislikeRepository.findByPlaceReviewIdAndUserId(placeReviewId, SecurityContextHolderUtil.getUserId());

                if(placeReviewLikeOpt.isPresent()){
                    pickLike = true;
                }else pickLike = false;

                if(placeReviewDislikeOpt.isPresent()){
                    pickDisLike = true;
                }else pickDisLike = false;

                PlaceReviewResDto placeReviewResDto = PlaceReviewResDto.builder()
                        .id(placeReviewId)
                        .userId(placeReview.getUserId())
                        .userProfileName(user.getProfileName())
                        .createdAt(createdAt)
                        .ratings(ratings)
                        .content(content)
                        .like(likeCount)
                        .dislike(disLikeCount)
                        .myReview(myReview)
                        .pickLike(pickLike)
                        .pickDisLike(pickDisLike)
                        .build();

                results.add(placeReviewResDto);
            }
        }

        return results;
    }

    private PlaceReview verifyPlaceReview(Optional<PlaceReview> placeReviewRepository) {
        Optional<PlaceReview> placeReviewOpt = placeReviewRepository;
        if(!placeReviewOpt.isPresent()){
            throw new PlaceReviewNotFoundException("Wrong placeReviewId");
        }
        PlaceReview placeReview = placeReviewOpt.get();
        if(placeReview.getUserId() == SecurityContextHolderUtil.getUserId()){
            throw new CannotRateReviewException("자신의 리뷰는 평가할 수 없습니다.");
        }
        return placeReview;
    }


}

