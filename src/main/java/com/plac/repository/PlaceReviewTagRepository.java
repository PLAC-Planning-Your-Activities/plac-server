package com.plac.repository;

import com.plac.domain.place_review.PlaceReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceReviewTagRepository extends JpaRepository<PlaceReviewTag, Long> {
    Optional<PlaceReviewTag> findByTagName(String tagName);
}
