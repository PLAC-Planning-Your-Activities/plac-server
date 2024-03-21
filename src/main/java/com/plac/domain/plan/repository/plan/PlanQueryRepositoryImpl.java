package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.plan.entity.QBookmarkPlan.bookmarkPlan;
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

    @Override
    public List<Plan> findFilteredPlan(String destinationName, String placeName, String sortBy, Integer ageRange, String gender, List<String> tags, long tagCount, Pageable pageable) {
        return null;
    }

    @Override
    public List<Long> findPlanIdsByDestinationName(String destinationName) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .where(plan.destinationName.eq(destinationName))
                .orderBy(plan.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByUserAgeRange(List<Long> planIdList, int ageRange) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .join(plan.user)
                .where(plan.id.in(planIdList)
                        .and(plan.user.ageRange.eq(ageRange)))
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByUserGender(List<Long> planIdList, String gender) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .join(plan.user)
                .where(plan.id.in(planIdList)
                        .and(plan.user.gender.eq(gender)))
                .fetch();
    }

    @Override
    public List<Plan> findPlansIn(List<Long> planIdList) {
        return jpaQueryFactory.select(plan)
                .from(plan)
                .where(plan.id.in(planIdList))
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByPlanDibsDesc(List<Long> planIdList) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .leftJoin(planDibs).on(plan.id.eq(planDibs.planId))
                .where(plan.id.in(planIdList))
                .groupBy(plan.id)
                .orderBy(planDibs.planId.count().desc())
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByBookmarkPlanDesc(List<Long> planIdList) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .leftJoin(bookmarkPlan).on(plan.id.eq(bookmarkPlan.plan.id))
                .where(plan.id.in(planIdList))
                .groupBy(plan.id)
                .orderBy(bookmarkPlan.plan.id.count().desc())
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByCreatedAtDesc(List<Long> planIdList) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .where(plan.id.in(planIdList))
                .orderBy(plan.createdAt.desc())
                .fetch();
    }
}
