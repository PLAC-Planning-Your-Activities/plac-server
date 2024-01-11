package com.plac.domain.place.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfo {
    private String placeName;
    private String type;
    private Long kakaoPlaceId;
    private String imageUrl;
    private BigDecimal x;
    private BigDecimal y;
    private String address;
}
