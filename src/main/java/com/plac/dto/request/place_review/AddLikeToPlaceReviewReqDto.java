package com.plac.dto.request.place_review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddLikeToPlaceReviewReqDto {

    private Long placeReviewId;
    private boolean like;
    private boolean dislike;
}
