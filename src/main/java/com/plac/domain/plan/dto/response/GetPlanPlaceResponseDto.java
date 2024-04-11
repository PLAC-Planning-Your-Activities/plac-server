package com.plac.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlanPlaceResponseDto {

    private String type;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
}
