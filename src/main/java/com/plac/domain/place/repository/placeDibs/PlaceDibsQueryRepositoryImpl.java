package com.plac.domain.place.repository.placeDibs;

import com.plac.domain.place.entity.PlaceDibs;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.plac.domain.place.entity.QPlaceDibs.placeDibs;

@Repository
public class PlaceDibsQueryRepositoryImpl implements PlaceDibsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceDibsQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<PlaceDibs> findByKakaoPlaceId(Long kakaoPlaceId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(placeDibs)
                .where(placeDibs.kakaoPlaceId.eq(kakaoPlaceId))
                .fetchOne());
    }

    @Override
    public Optional<PlaceDibs> findDibsByUserIdAndKakaoPlaceId(Long userId, Long kakaoPlaceId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(placeDibs)
                .where(placeDibs.userId.eq(userId)
                        .and(placeDibs.kakaoPlaceId.eq(kakaoPlaceId)))
                .fetchOne());
    }
}
