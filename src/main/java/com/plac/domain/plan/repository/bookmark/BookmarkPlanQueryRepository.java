package com.plac.domain.plan.repository.bookmark;

import com.plac.domain.plan.entity.BookmarkPlan;

import java.util.List;
import java.util.Optional;

public interface BookmarkPlanQueryRepository {

    List<BookmarkPlan> findBookmarskPlansByUserId(Long userId);

    Optional<BookmarkPlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<BookmarkPlan> findBookmarksPlansByPlanId(Long planId);
}
