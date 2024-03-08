package com.plac.domain.plan.repository.planTag;

import com.plac.domain.plan.entity.PlanTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {

    Optional<PlanTag> findByTagName(String tagName);
}
