package com.plac.domain.place.repository.placeDibs;

import com.plac.domain.place.entity.PlaceDibs;

import java.util.Optional;

public interface PlaceQueryRepository {

    Optional<PlaceDibs> findDibsByUserIdAndKakaoPlaceId(Long userId, Long kakaoPlaceId);
}
