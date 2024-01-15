package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository <Plan, Long> {

    Optional<Plan> findByName(String name);

    List<Plan> findByDestinationName(String destinationName);
}
