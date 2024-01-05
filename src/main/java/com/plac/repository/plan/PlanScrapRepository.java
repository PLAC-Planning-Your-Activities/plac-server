package com.plac.repository.plan;

import com.plac.domain.plan.PlanScrap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlanScrapRepository extends MongoRepository<PlanScrap, String>, PlanScrapRepositoryCutom {

    Optional<PlanScrap> findByPlanIdAndUserId(String planId, Long userId);

    boolean existsByPlanIdAndUserId(String planId, Long userId);

    Set<PlanScrap> findByUserIdAndPlanIdIn(Long userId, List<String> planIds);

    List<PlanScrap> findByUserId(Long userId);
}
