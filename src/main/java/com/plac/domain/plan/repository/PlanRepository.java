package com.plac.domain.plan.repository;

import com.plac.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository <Plan, Long> {

    Optional<Plan> findByName(String name);

    @Query("select p from Plan p where p.destinationName= :destinationName and p.open = true and p.isDeleted = false")
    List<Plan> findByDestinationName(@Param("destinationName") String destinationName);

    @Query("select p from Plan p where p.user.id = :userId and p.isDeleted = false")
    List<Plan> findByUserId(@Param("userId") Long userId);

    @Query("select p from Plan p where p.user.id = :userId and p.isDeleted = false and p.open = true")
    List<Plan> findByUserIdAndOpen(@Param("userId") Long userId);

    List<Plan> findByIdIn(List<Long> planIds);
}
