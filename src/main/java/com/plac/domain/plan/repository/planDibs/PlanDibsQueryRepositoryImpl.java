package com.plac.domain.plan.repository.planDibs;

import com.plac.domain.plan.entity.PlanDibs;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.plac.domain.plan.entity.QPlanDibs.planDibs;

@Repository
public class PlanDibsQueryRepositoryImpl implements PlanDibsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlanDibsQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<PlanDibs> findDibsByUserIdAndPlanId(Long userId, Long planId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(planDibs)
                        .where(planDibs.planId.eq(planId)
                                .and(planDibs.userId.eq(userId)))
                        .fetchOne()
        );
    }
}
