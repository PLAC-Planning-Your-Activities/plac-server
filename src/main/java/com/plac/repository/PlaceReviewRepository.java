package com.plac.repository;

import com.plac.domain.place_review.PlaceReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {
    @Query("SELECT AVG(pr.ratings.totalRating) FROM PlaceReview pr WHERE pr.placeId = :placeId")
    Float findAverageTotalRatingByPlaceId(@Param("placeId") Long placeId);

    @Query("SELECT ROUND(COUNT(pr), 0) FROM PlaceReview pr WHERE pr.placeId = :placeId")
    int countByPlaceId(@Param("placeId") Long placeId);

    @Query("SELECT pr FROM PlaceReview pr LEFT JOIN pr.placeReviewLikes prl " +
            "GROUP BY pr.id " +
            "ORDER BY COUNT(prl.id) DESC, pr.createdAt DESC")
    Page<PlaceReview> findAllOrderByLikesDesc(Pageable pageable);

    @Query("SELECT pr FROM PlaceReview pr WHERE pr.placeId = :placeId ORDER BY pr.createdAt DESC")
    Page<PlaceReview> findAllOrderByCreatedAtDesc(@Param("placeId") Long placeId, Pageable pageable);

    @Query("SELECT pr FROM PlaceReview pr ORDER BY pr.ratings.totalRating DESC")
    Page<PlaceReview> findAllOrderByTotalRatingDesc(Pageable pageable);

    @Query("SELECT pr FROM PlaceReview pr ORDER BY pr.ratings.totalRating ASC")
    Page<PlaceReview> findAllOrderByTotalRatingAsc(Pageable pageable);

    List<PlaceReview> findByPlaceIdIn(List<Long> placeId);
}
