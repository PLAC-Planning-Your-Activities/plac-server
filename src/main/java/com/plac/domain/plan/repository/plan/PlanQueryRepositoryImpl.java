package com.plac.domain.plan.repository.plan;

import com.plac.domain.place.entity.Place;
import com.plac.domain.plan.dto.response.GetPlanPlaceResponseDto;
import com.plac.domain.plan.entity.Plan;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.plac.domain.place.entity.QPlace.place;
import static com.plac.domain.plan.entity.QFavoritePlan.favoritePlan;
import static com.plac.domain.plan.entity.QPlan.plan;
import static com.plac.domain.plan.entity.QPlanDibs.planDibs;
import static com.plac.domain.plan.entity.QPlanPlaceMapping.planPlaceMapping;

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
                .on(plan.id.eq(planDibs.planId))
                .where(planDibs.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<GetPlanPlaceResponseDto> findPlaceDetailsByPlanId(Long planId) {
        List<Tuple> qr = jpaQueryFactory.select(
                        place.type,
                        place.placeName,
                        place.thumbnailImageUrl,
                        place.streetNameAddress)
                .from(place)
                .leftJoin(planPlaceMapping)
                .on(place.id.eq(planPlaceMapping.place.id))
                .where(planPlaceMapping.plan.id.eq(planId))
                .fetch();

        return qr.stream().map(
                tuple ->
                        new GetPlanPlaceResponseDto(
                                tuple.get(place.type),
                                tuple.get(place.placeName),
                                tuple.get(place.thumbnailImageUrl),
                                tuple.get(place.streetNameAddress)
                        )
        ).collect(Collectors.toList());
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
    public List<Long> findPlanIdsByUserAgeGroup(List<Long> planIdList, int ageGroup) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .join(plan.user)
                .where(plan.id.in(planIdList)
                        .and(plan.user.ageGroup.eq(ageGroup)))
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
                .join(planDibs).on(plan.id.eq(planDibs.planId))
                .where(plan.id.in(planIdList))
                .groupBy(plan.id)
                .orderBy(planDibs.planId.count().desc())
                .fetch();
    }

    @Override
    public List<Long> findPlanIdsByFavoritePlanDesc(List<Long> planIdList) {
        return jpaQueryFactory.select(plan.id)
                .from(plan)
                .leftJoin(favoritePlan).on(plan.id.eq(favoritePlan.plan.id))
                .where(plan.id.in(planIdList))
                .groupBy(plan.id)
                .orderBy(favoritePlan.plan.id.count().desc())
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
