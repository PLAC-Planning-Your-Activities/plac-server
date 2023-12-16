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
    private Long placeId;
    private String placeName;
    private String streetNameAddress;
    private int reviewCount;
    private float totalRatings;
}
