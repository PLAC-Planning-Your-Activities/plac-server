package com.plac.repository;

import com.plac.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByKakaoPlaceId(Long kakaoPlaceId);

    List<Place> findByIdIn(List<Long> placeId);

    long countByIdIn(List<Long> ids);
}
