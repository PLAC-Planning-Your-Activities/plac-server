package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.BookmarkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkPlanRepository extends JpaRepository<BookmarkPlan, Long> {

    List<BookmarkPlan> findByUserId(Long userId);

    Optional<BookmarkPlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<BookmarkPlan> findByPlanId(Long planId);

    @Query("SELECT bp.plan.id, COUNT(bp) FROM BookmarkPlan bp WHERE bp.plan.id IN :planIds GROUP BY bp.plan.id")
    List<Object[]> countByPlanIds(@Param("planIds") Set<Long> planIds);
}
