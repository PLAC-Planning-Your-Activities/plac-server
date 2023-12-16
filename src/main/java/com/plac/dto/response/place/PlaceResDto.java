package com.plac.dto.response.place;

import com.plac.domain.Place;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResDto {
    private Long kakaoPlaceId;
    private Long placPlaceId;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
    private float totalRating;
    private int reviewCount;
    private BigDecimal x;
    private BigDecimal y;

}
