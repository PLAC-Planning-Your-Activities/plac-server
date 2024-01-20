package com.plac.domain.place.repository;

import com.plac.domain.place.entity.PlaceLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceLikeRepository extends JpaRepository <PlaceLike, Long> {
    List<PlaceLike> findByUserId(Long userId);
}
