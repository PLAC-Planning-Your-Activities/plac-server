package com.plac.dto.request.place_review;

import com.plac.domain.place_review.Ratings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReviewReqDto {

    @Embedded
    private Ratings ratings;

    private Long kakaoPlaceId;
    private Long placeId;
    private String content;
    private List<String> pictureUrl;
    private List<String> tags;
}
