package com.plac.domain.plan.repository.planPlaceMapping;

import com.plac.domain.place.entity.Place;
import com.plac.domain.plan.entity.PlanPlaceMapping;

import java.util.List;

public interface PlanPlaceQueryMappingRepository {

    List<PlanPlaceMapping> findByPlanId(Long planId);

    List<Place> findPlacesByPlanId(Long planId);
}
