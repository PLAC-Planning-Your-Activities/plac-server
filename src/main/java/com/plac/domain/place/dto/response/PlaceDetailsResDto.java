package com.plac.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetailsResDto {
    private Long placeId;
    private String placeName;
    private String streetNameAddress;
}
