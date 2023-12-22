package com.plac.repository.plan;

import com.plac.domain.plan.PlanScrap;

import java.util.List;

public interface PlanScrapRepositoryCutom {
    List<PlanScrap> findPlanLikesByUserIdWithPaging(Long userId, String beforePlanId, int pageSize);
}
