package com.plac.service.place_review;

import com.plac.domain.place_review.PlaceReviewTag;
import com.plac.dto.request.place_review.PlaceReviewRateReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.dto.response.place_review.PlaceReviewResDto;

import java.util.List;

public interface PlaceReviewService {

    void writeReview(PlaceReviewReqDto req);

    PlaceReviewTag createPlaceReviewTag(String tagName);

    void addLikeToPlaceReview(PlaceReviewRateReqDto req);
    void addDisLikeToPlaceReview(PlaceReviewRateReqDto req);
    List<PlaceReviewResDto> getPlaceReviews(Long placeId, String sortBy, int page);
}
