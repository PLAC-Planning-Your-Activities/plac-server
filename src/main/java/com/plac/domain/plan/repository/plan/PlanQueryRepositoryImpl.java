package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.QPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.plan.entity.QPlan.plan;

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
}
