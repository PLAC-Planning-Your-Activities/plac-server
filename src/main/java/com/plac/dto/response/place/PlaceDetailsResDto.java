package com.plac.dto.response.place;

import com.plac.domain.place_review.Ratings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetailsResDto {
    private Long kakaoPlaceId;
    private Long placPlaceId;
    private BigDecimal x;
    private BigDecimal y;
    private int reviewCount;
    private Ratings averageRatings;
    private String sortBy;
    private String page;
}
