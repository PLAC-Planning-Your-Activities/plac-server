package com.plac.domain.place.repository.place;

import com.plac.domain.place.entity.Place;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.plac.domain.place.entity.QPlace.place;
import static com.plac.domain.place.entity.QPlaceDibs.placeDibs;

@Repository
public class PlaceQueryRepositoryImpl implements PlaceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Place> findPlaceDibsByUserId(long userId) {
        return jpaQueryFactory.selectFrom(place)
                .leftJoin(placeDibs)
                .on(place.kakaoPlaceId.eq(placeDibs.kakaoPlaceId))
                .where(placeDibs.userId.eq(userId))
                .orderBy(place.placeName.asc())
                .fetch();
    }
}
