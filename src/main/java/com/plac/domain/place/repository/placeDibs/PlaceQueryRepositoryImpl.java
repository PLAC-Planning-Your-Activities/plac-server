package com.plac.domain.place.repository.placeDibs;

import com.plac.domain.place.entity.PlaceDibs;
import com.plac.domain.place.entity.QPlaceDibs;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.plac.domain.place.entity.QPlaceDibs.placeDibs;

@Repository
public class PlaceQueryRepositoryImpl implements PlaceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<PlaceDibs> findDibsByUserIdAndKakaoPlaceId(Long userId, Long kakaoPlaceId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(placeDibs)
                .where(placeDibs.userId.eq(userId)
                        .and(placeDibs.kakaoPlaceId.eq(kakaoPlaceId)))
                .fetchOne());
    }
}
