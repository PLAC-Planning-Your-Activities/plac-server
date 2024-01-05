package com.plac.repository.plan;

import com.plac.domain.plan.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlanRepository extends MongoRepository<Plan, Long>, PlanRepositoryCustom {
    Optional<Plan> findById(String id);

    List<Plan> findByIdIn(Set<String> ids);

    List<Plan> findByIdIn(List<String> ids);
}
