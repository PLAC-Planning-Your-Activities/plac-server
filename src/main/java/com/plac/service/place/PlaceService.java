package com.plac.service.place;

import com.plac.domain.Place;
import com.plac.dto.request.place.KakaoPlaceInfo;
import com.plac.dto.request.place.PlaceReqDto;
import com.plac.dto.response.place.PlaceResDto;

import java.util.List;

public interface PlaceService {

    float getTotalRatingAverage(Long placeId);
    List<PlaceResDto> getPlaceSummaryInfo(PlaceReqDto req);
    Place createNewPlace(KakaoPlaceInfo req);

}
