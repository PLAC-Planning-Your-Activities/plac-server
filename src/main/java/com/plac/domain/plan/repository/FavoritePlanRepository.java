package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.FavoritePlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePlanRepository extends JpaRepository<FavoritePlan, Long> {

    List<FavoritePlan> findByUserId(Long userId);

    Optional<FavoritePlan> findByUserIdAndPlanId(Long userId, Long planId);
}
