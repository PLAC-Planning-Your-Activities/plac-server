package com.plac.domain.plan.repository.planPlaceMapping;

import com.plac.domain.plan.entity.PlanPlaceMapping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.plan.entity.QPlanPlaceMapping.planPlaceMapping;

@Repository
public class PlanPlaceQueryMappingRepositoryImpl implements PlanPlaceQueryMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlanPlaceQueryMappingRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PlanPlaceMapping> findByPlanId(Long planId) {
        return jpaQueryFactory.selectFrom(planPlaceMapping)
                .where(planPlaceMapping.plan.id.eq(planId))
                .fetch();
    }
}
