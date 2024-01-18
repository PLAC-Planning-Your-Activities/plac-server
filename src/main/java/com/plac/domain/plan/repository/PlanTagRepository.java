package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.PlanTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {

    Optional<PlanTag> findByTagName(String tagName);

    @Query("SELECT pt.id, pt.tagName FROM PlanTag pt WHERE pt.id IN :planTagIds")
    List<Object[]> findTagNamesByPlanTagIds(@Param("planTagIds") Set<Long> planTagIds);
}
