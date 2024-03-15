package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.CreatePlacesRequest;
import com.plac.domain.place.dto.request.KakaoPlaceInfo;
import com.plac.domain.place.dto.request.PlaceDibsRequestDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceDibsRepository placeDibsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void triggerDibsMyListPlace(KakaoPlaceInfo dto) {
        final User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음 예외추가"));
        final long userId = user.getId();

        Optional<PlaceDibs> getDibsPlace = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(userId, dto.getKakaoPlaceId());
        if (getDibsPlace.isEmpty()) {
            // TODO : Place Type 수정 필요
            createPlace(dto);
            addDibsPlace(userId, dto.getKakaoPlaceId());
        } else {
            PlaceDibs placeDibs = getDibsPlace.orElseThrow(() -> new RuntimeException("찜되어 있지 않음 예외 추가"));
            deleteDibsPlace(placeDibs);
        }
    }

    @Transactional
    public void deleteMyListPlace(final long kakaoPlaceId) {
        final User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음 예외추가"));
        long userId = user.getId();

        if (isExistedPlace(kakaoPlaceId)) throw new RuntimeException("예외 추가");

        PlaceDibs placeDibs = placeDibsRepository.
                findDibsByUserIdAndKakaoPlaceId(userId, kakaoPlaceId)
                .orElseThrow(() -> new RuntimeException("찜되어 있지 않음 예외 추가"));

        deleteDibsPlace(placeDibs);
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

    private void addDibsPlace(long userId, long kakaoPlaceId) {
        PlaceDibs placeDibs = PlaceDibs.create(kakaoPlaceId, userId);
        placeDibsRepository.save(placeDibs);
    }

    private void deleteDibsPlace(PlaceDibs placeDibs) {
        placeDibsRepository.delete(placeDibs);
    }

    private void createPlace(KakaoPlaceInfo placeInfo) {
        if (!isExistedPlace(placeInfo.getKakaoPlaceId())) return;

        Place place = Place.create(
                null, placeInfo.getKakaoPlaceId(), placeInfo.getPlaceName(),
                placeInfo.getThumbnailImageUrl(), placeInfo.getStreetNameAddress(),
                placeInfo.getX(), placeInfo.getY());

        placeRepository.save(place);
    }

    private boolean isExistedPlace(final long kakaoPlaceId) {
        return placeRepository.findByKakaoPlaceId(kakaoPlaceId).isEmpty();
    }
}
