package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.BookmarkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkPlanRepository extends JpaRepository<BookmarkPlan, Long> {

    List<BookmarkPlan> findByUserId(Long userId);

    Optional<BookmarkPlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<BookmarkPlan> findByPlanId(Long planId);
}
