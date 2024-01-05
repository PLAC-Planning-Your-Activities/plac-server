package com.plac.repository.plan;

import com.plac.domain.plan.PlanLike;

import java.util.List;

public interface PlanLikeRepositoryCustom {
    List<PlanLike> findPlanLikesByUserIdWithPaging(Long userId, String beforePlanId, int pageSize);
}
