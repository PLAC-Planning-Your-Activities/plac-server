package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.PlanTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanTagMappingRepository extends JpaRepository<PlanTagMapping, Long> {

    @Query("select mapping from PlanTagMapping mapping where mapping.plan.id = :planId")
    List<PlanTagMapping> findByPlanId(@Param("planId") Long planId);

    @Query("select mapping from PlanTagMapping mapping where mapping.planTagId = :planTagId")
    List<PlanTagMapping> findByPlanTagId(@Param("planTagId") Long planTagId);
}