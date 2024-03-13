package com.plac.domain.place.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreatePlaceRequest {
    private Long kakaoPlaceId;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
    private BigDecimal x;
    private BigDecimal y;
}
