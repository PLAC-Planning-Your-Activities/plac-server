package com.plac.service.place;

import com.plac.domain.Place;
import com.plac.dto.request.place.KakaoPlaceInfo;
import com.plac.dto.request.place.PlaceReqDto;
import com.plac.dto.response.place.PlaceDetailsResDto;
import com.plac.dto.response.place.PlaceResDto;

import java.util.List;

public interface PlaceService {

    float getTotalRatingAverage(Long placeId);

    List<PlaceResDto> getPlacesSummaryInfo(PlaceReqDto req);

    Place createNewPlace(KakaoPlaceInfo req);

    PlaceDetailsResDto getPlaceDetails(Long placeId);

    List<PlaceResDto> getPlaces(List<Long> placeIds);

    boolean placesExist(List<Long> placeIds);
}
