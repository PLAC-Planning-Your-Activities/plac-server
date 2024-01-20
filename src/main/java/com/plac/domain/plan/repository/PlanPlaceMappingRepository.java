package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.PlanPlaceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanPlaceMappingRepository extends JpaRepository<PlanPlaceMapping, Long> {

    @Query("select mapping from PlanPlaceMapping mapping where mapping.plan.id = :planId")
    List<PlanPlaceMapping> findByPlanId(@Param("planId") Long planId);

    List<PlanPlaceMapping> findByPlanIdIn(List<Long> planIds);
}
