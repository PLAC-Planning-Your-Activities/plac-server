package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanQueryRepository {

    List<Plan> findPlansByDestinationName(String destinationName);

    List<Plan> findPlanDibsByUserId(Long userId);

    List<Plan> findFilteredPlan(String destinationName, String placeName, String sortBy, Integer ageRange,
                                        String gender, List<String> tags, long tagCount, Pageable pageable);

    List<Long> findPlanIdsByDestinationName(String destinationName);

    List<Long> findPlanIdsByUserAgeRange(List<Long> planIdList, int ageRange);

    List<Long> findPlanIdsByUserGender(List<Long> planIdList, String gender);

    List<Long> findPlanIdsByCreatedAtDesc(List<Long> planIdList);

    List<Plan> findPlansIn(List<Long> planIdList);

    List<Long> findPlanIdsByPlanDibsDesc(List<Long> planIdList);

    List<Long> findPlanIdsByBookmarkPlanDesc(List<Long> planIdList);
}
