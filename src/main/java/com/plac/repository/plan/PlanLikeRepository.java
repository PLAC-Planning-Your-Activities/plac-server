package com.plac.repository.plan;

import com.plac.domain.plan.PlanLike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlanLikeRepository extends MongoRepository<PlanLike, String>, PlanLikeRepositoryCustom {
    Optional<PlanLike> findByPlanIdAndUserId(String planId, Long userId);

    boolean existsByPlanIdAndUserId(String planId, Long userId);

    Set<PlanLike> findByUserIdAndPlanIdIn(Long userId, List<String> planIds);

    List<PlanLike> findByUserId(Long userId);
}
