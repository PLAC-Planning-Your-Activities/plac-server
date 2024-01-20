package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.FavoritePlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FavoritePlanRepository extends JpaRepository<FavoritePlan, Long> {

    List<FavoritePlan> findByUserId(Long userId);

    Optional<FavoritePlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<FavoritePlan> findByPlanId(Long planId);

    @Query("SELECT fp.plan.id, SUM(CASE WHEN fp.favorite = true THEN 1 ELSE 0 END) AS totalLikes " +
            "FROM FavoritePlan fp " +
            "WHERE fp.plan.isDeleted = false AND fp.plan.open = true "+
            "GROUP BY fp.plan.id " +
            "ORDER BY totalLikes DESC")
    List<Object[]> findPlansOrderByTotalLikesDesc(Pageable pageable);


    @Query("SELECT fp.plan.id, COUNT(fp) FROM FavoritePlan fp " +
            "WHERE fp.plan.isDeleted = false " +
            "AND fp.plan.id IN :planIds GROUP BY fp.plan.id")
    List<Object[]> countByPlanIds(@Param("planIds") Set<Long> planIds);

    List<FavoritePlan> findByPlanIdIn(List<Long> planIds);
}
