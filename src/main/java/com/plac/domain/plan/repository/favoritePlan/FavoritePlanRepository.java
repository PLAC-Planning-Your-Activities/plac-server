package com.plac.domain.plan.repository.favoritePlan;

import com.plac.domain.plan.entity.FavoritePlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoritePlanRepository extends JpaRepository<FavoritePlan, Long> {

    List<FavoritePlan> findByUserId(Long userId);

    Optional<FavoritePlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<FavoritePlan> findByPlanId(Long planId);

    @Query("SELECT fp.plan.id, SUM(CASE WHEN fp.favorite = true THEN 1 ELSE 0 END) AS totalLikes " +
            "FROM FavoritePlan fp " +
            "GROUP BY fp.plan.id " +
            "ORDER BY totalLikes DESC")
    List<Object[]> findPlansOrderByTotalLikesDesc(Pageable pageable);
}
