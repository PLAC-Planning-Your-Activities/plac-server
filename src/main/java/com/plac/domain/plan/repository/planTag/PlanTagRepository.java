package com.plac.domain.plan.repository.planTag;

import com.plac.domain.plan.entity.PlanTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {

    Optional<PlanTag> findByTagName(String tagName);

    @Query("SELECT ptm.plan.id FROM PlanTagMapping ptm " +
            "JOIN ptm.planTag pt " +
            "WHERE ptm.plan.id IN :planIdList " +
            "AND pt.tagName IN :tagList " +
            "GROUP BY ptm.plan.id " +
            "HAVING COUNT(pt.id) = :tagCount")
    List<Long> findPlanIdsWithAllTags(@Param("planIdList") List<Long> planIdList,
                                      @Param("tagList") List<String> tagList,
                                      @Param("tagCount") long tagCount);
}
