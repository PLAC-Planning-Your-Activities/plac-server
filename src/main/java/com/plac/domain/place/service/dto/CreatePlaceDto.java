package com.plac.domain.place.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceDto {
    private Long kakaoPlaceId;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
    private BigDecimal x;
    private BigDecimal y;
}
