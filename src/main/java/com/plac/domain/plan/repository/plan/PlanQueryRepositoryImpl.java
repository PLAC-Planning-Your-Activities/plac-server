package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.plan.entity.QPlan.plan;
import static com.plac.domain.plan.entity.QPlanDibs.planDibs;

@Repository
public class PlanQueryRepositoryImpl implements PlanQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlanQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Plan> findPlansByDestinationName(String destinationName) {
        return jpaQueryFactory.selectFrom(plan)
                .where(plan.destinationName.eq(destinationName))
                .fetch();
    }

    @Override
    public List<Plan> findPlanDibsByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(plan)
                .leftJoin(planDibs)
                .on((plan.id.eq(planDibs.planId)))
                .where(planDibs.userId.eq(userId))
                .orderBy(planDibs.createdAt.desc())
                .fetch();
    }
}
