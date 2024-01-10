package com.plac.domain.place.dto.response;

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
    private BigDecimal x;
    private BigDecimal y;

}
