package com.plac.repository.plan;

import com.plac.domain.plan.Plan;

import java.util.List;

public interface PlanRepositoryCustom {
    List<Plan> findPlansByUserIdWithPaging(Long userId, String beforePlanId, int pageSize);
}
