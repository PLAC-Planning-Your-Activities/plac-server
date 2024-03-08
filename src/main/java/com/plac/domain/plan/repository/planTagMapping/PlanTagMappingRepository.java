package com.plac.domain.plan.repository.planTagMapping;

import com.plac.domain.plan.entity.PlanTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanTagMappingRepository extends JpaRepository<PlanTagMapping, Long>, PlanTagMappingQueryRepository {
}