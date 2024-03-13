package com.plac.domain.place.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePlacesRequest {
    private List<KakaoPlaceInfo> positionList;

    @Builder
    public CreatePlacesRequest(List<KakaoPlaceInfo> positionList) {
        this.positionList = positionList;
    }
}
