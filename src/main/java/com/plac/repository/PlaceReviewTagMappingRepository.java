package com.plac.repository;

import com.plac.domain.place_review.PlaceReviewTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceReviewTagMappingRepository extends JpaRepository<PlaceReviewTagMapping, Long> {
}
