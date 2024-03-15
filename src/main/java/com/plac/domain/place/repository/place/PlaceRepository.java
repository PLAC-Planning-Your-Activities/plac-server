package com.plac.domain.place.repository.place;

import com.plac.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceQueryRepository {
    Optional<Place> findByKakaoPlaceId(Long kakaoPlaceId);
}
