package com.plac.service.place_review;

import com.plac.domain.Place;
import com.plac.domain.place_review.PlaceReview;
import com.plac.domain.place_review.PlaceReviewImage;
import com.plac.domain.place_review.PlaceReviewTag;
import com.plac.domain.place_review.PlaceReviewTagMapping;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.exception.place.WrongKakaoPlaceIdException;
import com.plac.exception.place.WrongPlaceIdException;
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


}

