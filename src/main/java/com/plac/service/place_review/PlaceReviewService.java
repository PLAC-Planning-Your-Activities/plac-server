package com.plac.service.place_review;

import com.plac.domain.place_review.PlaceReviewTag;
import com.plac.dto.request.place_review.AddLikeToPlaceReviewReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;

public interface PlaceReviewService {

    void writeReview(PlaceReviewReqDto req);

    PlaceReviewTag createPlaceReviewTag(String tagName);

    void ratePlaceReview(AddLikeToPlaceReviewReqDto req);
}
