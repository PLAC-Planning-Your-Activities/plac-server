package com.plac.domain.plan.repository.planDibs;

import com.plac.domain.plan.entity.PlanDibs;

import java.util.Optional;

public interface PlanDibsQueryRepository {

    Optional<PlanDibs> findDibsByUserIdAndPlanId(Long userId, Long planId);
}
