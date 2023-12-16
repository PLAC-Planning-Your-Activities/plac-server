package com.plac.service.place;

import com.plac.domain.Place;
import com.plac.dto.request.place.KakaoPlaceInfo;
import com.plac.dto.request.place.PlaceReqDto;
import com.plac.dto.response.place.PlaceResDto;
import com.plac.repository.PlaceRepository;
import com.plac.repository.PlaceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceRepository placeRepository;

    @Override
    public float getTotalRatingAverage(Long placeId) {
        float totalRatingAverage = placeReviewRepository.findAverageTotalRatingByPlaceId(placeId);
        return totalRatingAverage;
    }

    @Override
    public List<PlaceResDto> getPlacesSummaryInfo(PlaceReqDto req) {
        List<KakaoPlaceInfo> placeInfoList = req.getPositionList();
        List<PlaceResDto> result = new ArrayList<>();

        for (KakaoPlaceInfo placeInfo : placeInfoList) {
            Place place = placeRepository.findByKakaoPlaceId(placeInfo.getKakaoPlaceId())
                    .orElseGet(() -> createNewPlace(placeInfo));
            PlaceResDto placeResDto = buildPlaceResDto(place, placeInfo);
            result.add(placeResDto);
        }

        return result;
    }

    @Override
    public Place createNewPlace(KakaoPlaceInfo req) {
        Place newPlace = Place.builder()
                .kakaoPlaceId(req.getKakaoPlaceId())
                .placeName(req.getPlaceName())
                .thumbnailImageUrl(req.getThumbnailImageUrl())
                .streetNameAddress(req.getStreetNameAddress())
                .x(req.getX())
                .y(req.getY())
                .build();
        return placeRepository.save(newPlace);
    }

    private PlaceResDto buildPlaceResDto(Place place, KakaoPlaceInfo placeInfo) {
        int reviewCount = placeReviewRepository.countByPlaceId(place.getId());

        Float averageTotalRating = placeReviewRepository.findAverageTotalRatingByPlaceId(place.getId());
        if (averageTotalRating == null) averageTotalRating = 0.0f;
        else{
            averageTotalRating = Math.round(averageTotalRating * 10) / 10.0f;
        }

        return PlaceResDto.builder()
                .kakaoPlaceId(placeInfo.getKakaoPlaceId())
                .placPlaceId(place.getId())
                .placeName(placeInfo.getPlaceName())
                .thumbnailImageUrl(placeInfo.getThumbnailImageUrl())
                .streetNameAddress(placeInfo.getStreetNameAddress())
                .totalRating(averageTotalRating)
                .reviewCount(reviewCount)
                .x(place.getX())
                .y(place.getY())
                .build();
    }

}
