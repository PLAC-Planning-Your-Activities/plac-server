package com.plac.domain.plan.repository.planTagMapping;

import com.plac.domain.plan.entity.PlanTagMapping;

import java.util.List;

public interface PlanTagMappingQueryRepository {

    List<PlanTagMapping> findTagsByPlanId(Long planId);

    List<PlanTagMapping> findByPlanTagId(Long planTagId);
}
