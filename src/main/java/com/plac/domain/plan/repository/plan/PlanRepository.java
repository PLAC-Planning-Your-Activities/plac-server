package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository <Plan, Long>, PlanQueryRepository {

    Optional<Plan> findByName(String name);

}
