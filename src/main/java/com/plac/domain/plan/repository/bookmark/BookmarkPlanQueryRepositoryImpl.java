package com.plac.domain.plan.repository.bookmark;

import com.plac.domain.plan.entity.BookmarkPlan;
import com.plac.domain.plan.entity.QBookmarkPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.plac.domain.plan.entity.QBookmarkPlan.bookmarkPlan;

@Repository
public class BookmarkPlanQueryRepositoryImpl implements BookmarkPlanQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookmarkPlanQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<BookmarkPlan> findBookmarskPlansByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(bookmarkPlan)
                .where(bookmarkPlan.user.id.eq(userId))
                .fetch();
    }

    @Override
    public Optional<BookmarkPlan> findByUserIdAndPlanId(Long userId, Long planId) {
        BookmarkPlan entity = jpaQueryFactory.selectFrom(bookmarkPlan)
                .where(bookmarkPlan.user.id.eq(userId)
                        .and(bookmarkPlan.plan.id.eq(planId)))
                .fetchOne();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<BookmarkPlan> findBookmarksPlansByPlanId(Long planId) {
        return jpaQueryFactory.selectFrom(bookmarkPlan)
                .where(bookmarkPlan.plan.id.eq(planId))
                .fetch();
    }
}
