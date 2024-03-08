package com.plac.domain.plan.repository.planTagMapping;

import com.plac.domain.plan.entity.PlanTagMapping;
import com.plac.domain.plan.entity.QPlanTagMapping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.plan.entity.QPlanTagMapping.planTagMapping;

@Repository
public class PlanTagMappingQueryRepositoryImpl implements PlanTagMappingQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlanTagMappingQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PlanTagMapping> findTagsByPlanId(Long planId) {
        return jpaQueryFactory.selectFrom(planTagMapping)
                .where(planTagMapping.plan.id.eq(planId))
                .fetch();
    }

    @Override
    public List<PlanTagMapping> findByPlanTagId(Long planTagId) {
        return null;
    }
}
