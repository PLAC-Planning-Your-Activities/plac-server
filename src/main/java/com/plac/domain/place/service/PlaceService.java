package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.CreatePlaceRequest;
import com.plac.domain.place.dto.request.KakaoPlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public void createPlaces(CreatePlaceRequest req) {
        List<KakaoPlaceInfo> positionList = req.getPositionList();

        /**
         * 쿼리 최적화 하기!! */
        for (KakaoPlaceInfo kakaoPlaceInfo : positionList) {
            Long kakaoPlaceId = kakaoPlaceInfo.getKakaoPlaceId();

            if (placeRepository.findByKakaoPlaceId(kakaoPlaceId).isPresent()) {
                continue;
            }

            Place newPlace = Place.builder()
                    .kakaoPlaceId(kakaoPlaceId)
                    .placeName(kakaoPlaceInfo.getPlaceName())
                    .thumbnailImageUrl(kakaoPlaceInfo.getThumbnailImageUrl())
                    .streetNameAddress(kakaoPlaceInfo.getStreetNameAddress())
                    .x(kakaoPlaceInfo.getX())
                    .y(kakaoPlaceInfo.getY())
                    .build();

            placeRepository.save(newPlace);
        }
    }
}
