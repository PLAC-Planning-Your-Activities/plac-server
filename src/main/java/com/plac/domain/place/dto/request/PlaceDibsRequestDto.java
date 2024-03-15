package com.plac.domain.place.dto.request;

import com.plac.domain.place.service.dto.CreatePlaceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceDibsRequestDto {

    private KakaoPlaceInfo placeInfo;
    public KakaoPlaceInfo toCreatePlaceDto() {
        return new KakaoPlaceInfo(placeInfo.getKakaoPlaceId(), placeInfo.getPlaceName(),
                getPlaceInfo().getThumbnailImageUrl(), placeInfo.getStreetNameAddress(), placeInfo.getX(), placeInfo.getY());
    }
}
