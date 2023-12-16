package com.plac.repository;

import com.plac.domain.place_review.PlaceReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceReviewLikeRepository extends JpaRepository <PlaceReviewLike, Long> {
    @Query("SELECT COUNT(prl) FROM PlaceReviewLike prl WHERE prl.placeReview.id = :placeReviewId")
    int countByPlaceReviewId(@Param("placeReviewId") Long placeReviewId);

    @Query("SELECT prl FROM PlaceReviewLike prl WHERE prl.placeReview.id = :placeReviewId AND prl.userId = :userId")
    Optional<PlaceReviewLike> findByPlaceReviewIdAndUserId(@Param("placeReviewId") Long placeReviewId, @Param("userId") Long userId);
}
