package com.plac.domain.plan.repository.planDibs;

import com.plac.domain.plan.entity.PlanDibs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanDibsRepository extends JpaRepository<PlanDibs, Long>, PlanDibsQueryRepository {
}
