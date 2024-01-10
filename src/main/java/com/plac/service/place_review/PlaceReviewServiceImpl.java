package com.plac.service.place_review;

import com.plac.domain.Place;
import com.plac.domain.user.entity.User;
import com.plac.domain.place_review.*;
import com.plac.domain.user.repository.UserRepository;
import com.plac.dto.request.place_review.PlaceReviewRateReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.dto.response.place_review.PlaceReviewResDto;
import com.plac.exception.place.WrongKakaoPlaceIdException;
import com.plac.exception.place.WrongPlaceIdException;
import com.plac.exception.place_review.CannotRateReviewException;
import com.plac.exception.place_review.PlaceReviewNotFoundException;
import com.plac.exception.user.UserNotFoundException;
import com.plac.repository.*;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

        if ("최신순".equals(sortBy)) {
            PageRequest pageRequest = PageRequest.of(page, 3);
            Page<PlaceReview> result = placeReviewRepository.findAllOrderByCreatedAtDesc(placeId, pageRequest);
            List<PlaceReview> placeReviews = result.getContent();
            results = placeReviews.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }

        return results;
    }

    private PlaceReviewResDto convertToDto(PlaceReview placeReview) {
        User user = userRepository.findById(placeReview.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Long placeReviewId = placeReview.getId();
        int likeCount = placeReviewLikeRepository.countByPlaceReviewId(placeReviewId);
        int disLikeCount = placeReviewDislikeRepository.countByPlaceReviewId(placeReviewId);
        long currentUserId = SecurityContextHolderUtil.getUserId();

        return PlaceReviewResDto.builder()
                .id(placeReviewId)
                .userId(placeReview.getUserId())
                .userProfileName(user.getProfileName())
                .createdAt(placeReview.getCreatedAt())
                .ratings(placeReview.getRatings())
                .content(placeReview.getContent())
                .like(likeCount)
                .dislike(disLikeCount)
                .myReview(placeReview.getUserId() == currentUserId)
                .pickLike(placeReviewLikeRepository.findByPlaceReviewIdAndUserId(placeReviewId, currentUserId).isPresent())
                .pickDisLike(placeReviewDislikeRepository.findByPlaceReviewIdAndUserId(placeReviewId, currentUserId).isPresent())
                .build();
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

