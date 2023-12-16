package com.plac.repository;

import com.plac.domain.place_review.PlaceReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceReviewLikeRepository extends JpaRepository <PlaceReviewLike, Long> {
}
