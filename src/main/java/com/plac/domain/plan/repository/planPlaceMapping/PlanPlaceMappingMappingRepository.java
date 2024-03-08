package com.plac.domain.plan.repository.planPlaceMapping;

import com.plac.domain.plan.entity.PlanPlaceMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanPlaceMappingMappingRepository extends JpaRepository<PlanPlaceMapping, Long>, PlanPlaceQueryMappingRepository {
}
