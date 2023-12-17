package com.plac.repository;

import com.plac.domain.place_review.PlaceReviewDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceReviewDislikeRepository extends JpaRepository<PlaceReviewDislike, Long> {
    @Query("SELECT COUNT(prdl) FROM PlaceReviewDislike prdl WHERE prdl.placeReview.id = :placeReviewId")
    int countByPlaceReviewId(@Param("placeReviewId") Long placeReviewId);

    @Query("SELECT prl FROM PlaceReviewDislike prl WHERE prl.placeReview.id = :placeReviewId AND prl.userId = :userId")
    Optional<PlaceReviewDislike> findByPlaceReviewIdAndUserId(@Param("placeReviewId") Long placeReviewId, @Param("userId") Long userId);
}
