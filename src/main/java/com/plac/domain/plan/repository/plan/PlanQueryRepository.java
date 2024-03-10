package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;

import java.util.List;

public interface PlanQueryRepository {

    List<Plan> findPlansByDestinationName(String destinationName);
}
