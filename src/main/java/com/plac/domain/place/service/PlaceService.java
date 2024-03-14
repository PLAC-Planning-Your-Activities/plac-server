package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.CreatePlacesRequest;
import com.plac.domain.place.dto.request.KakaoPlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.entity.PlaceDibs;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.place.repository.placeDibs.PlaceDibsRepository;
import com.plac.domain.place.service.dto.CreatePlaceDto;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceDibsRepository placeDibsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteMyListPlace(final long kakaoPlaceId) {
        final User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음 예외추가"));
        long userId = user.getId();

        Place kakaoPlace = placeRepository.findByKakaoPlaceId(kakaoPlaceId)
                .orElseThrow(() -> new RuntimeException("예외 추가"));

        PlaceDibs placeDibs = placeDibsRepository.
                findDibsByUserIdAndKakaoPlaceId(userId, kakaoPlace.getKakaoPlaceId())
                .orElseThrow(() -> new RuntimeException("찜되어 있지 않음 예외 추가"));

        placeDibsRepository.delete(placeDibs);
    }

    public void createPlaces(CreatePlacesRequest req) {
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

    private Place createPlace(CreatePlaceDto createPlaceDto) {
        Place place = Place.create(
                null, createPlaceDto.getKakaoPlaceId(), createPlaceDto.getPlaceName(),
                createPlaceDto.getThumbnailImageUrl(), createPlaceDto.getStreetNameAddress(),
                createPlaceDto.getX(), createPlaceDto.getY());

        return placeRepository.save(place);
    }
}
