package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository <Plan, Long> {
}
