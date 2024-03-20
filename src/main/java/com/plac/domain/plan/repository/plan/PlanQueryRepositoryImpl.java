package com.plac.domain.plan.repository.plan;

import com.plac.domain.plan.entity.Plan;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.place.entity.QPlace.place;
import static com.plac.domain.plan.entity.QBookmarkPlan.bookmarkPlan;
import static com.plac.domain.plan.entity.QPlan.plan;
import static com.plac.domain.plan.entity.QPlanDibs.planDibs;
import static com.plac.domain.plan.entity.QPlanPlaceMapping.planPlaceMapping;
import static com.plac.domain.plan.entity.QPlanTag.planTag;
import static com.plac.domain.plan.entity.QPlanTagMapping.planTagMapping;
import static com.plac.domain.user.entity.QUser.user;

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

    public List<Plan> findFilteredPlan(String destinationName, String placeName, String sortBy, int ageRange,
                                       String gender, List<String> tags, long tagCount, Pageable pageable) {
        JPAQuery<Plan> query = jpaQueryFactory.selectFrom(plan)
                .join(plan.user, user)
                .leftJoin(planPlaceMapping).on(plan.id.eq(planPlaceMapping.plan.id))
                .leftJoin(planPlaceMapping.place, place)
                .leftJoin(planDibs).on(plan.id.eq(planDibs.planId))
                .leftJoin(bookmarkPlan).on(plan.id.eq(bookmarkPlan.plan.id))
                .leftJoin(planTagMapping).on(plan.id.eq(planTagMapping.plan.id))
                .leftJoin(planTagMapping.planTag, planTag);

        BooleanExpression whereCondition = plan.destinationName.eq(destinationName)
                .and(user.ageRange.eq(ageRange))
                .and(user.gender.eq(gender));

        if (placeName != null && !placeName.isEmpty()) {
            whereCondition = whereCondition.and(place.placeName.like(placeName));
        }

        if (tags != null && !tags.isEmpty()) {
            whereCondition = whereCondition.and(planTag.tagName.in(tags))
                    .and(planTagMapping.planTag.id.countDistinct().eq(tagCount));
        }

        query.where(whereCondition);

        OrderSpecifier<?> orderBy = new OrderSpecifier<>(Order.DESC, plan.createdAt);
        if ("인기순".equals(sortBy)) {
            orderBy = new OrderSpecifier<>(Order.DESC, planDibs.count());
        } else if ("저장순".equals(sortBy)) {
            orderBy = new OrderSpecifier<>(Order.DESC, bookmarkPlan.count());
        }

        // Apply paging, fetch
        List<Plan> results = query.orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return results;
    }

}
