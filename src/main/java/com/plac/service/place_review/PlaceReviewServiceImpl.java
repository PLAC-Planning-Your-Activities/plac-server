package com.plac.service.place_review;

import com.plac.domain.Place;
import com.plac.domain.place_review.*;
import com.plac.dto.request.place_review.AddLikeToPlaceReviewReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.exception.place.WrongKakaoPlaceIdException;
import com.plac.exception.place.WrongPlaceIdException;
import com.plac.exception.place_review.CannotRateReviewException;
import com.plac.exception.place_review.PlaceReviewNotFoundException;
import com.plac.repository.*;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void ratePlaceReview(AddLikeToPlaceReviewReqDto req) {
        Optional<PlaceReview> placeReviewOpt = placeReviewRepository.findById(req.getPlaceReviewId());
        if(!placeReviewOpt.isPresent()){
            throw new PlaceReviewNotFoundException("Wrong placeReviewId");
        }

        PlaceReview placeReview = placeReviewOpt.get();
        if(placeReview.getUserId() == SecurityContextHolderUtil.getUserId()){
            throw new CannotRateReviewException("자신의 리뷰는 평가할 수 없습니다.");
        }

        if(req.isLike()){
            PlaceReviewLike placeReviewLike = PlaceReviewLike.builder()
                    .placeReview(placeReview)
                    .userId(SecurityContextHolderUtil.getUserId()).build();
            placeReviewLikeRepository.save(placeReviewLike);
        }else if(req.isDislike()){
            PlaceReviewDislike placeReviewDislike = PlaceReviewDislike.builder()
                    .placeReview(placeReview)
                    .userId(SecurityContextHolderUtil.getUserId()).build();
            placeReviewDislikeRepository.save(placeReviewDislike);
        }
    }

}

