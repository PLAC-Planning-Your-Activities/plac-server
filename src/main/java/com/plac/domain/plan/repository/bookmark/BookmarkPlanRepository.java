package com.plac.domain.plan.repository.bookmark;

import com.plac.domain.plan.entity.BookmarkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkPlanRepository extends JpaRepository<BookmarkPlan, Long>, BookmarkPlanQueryRepository {
}
