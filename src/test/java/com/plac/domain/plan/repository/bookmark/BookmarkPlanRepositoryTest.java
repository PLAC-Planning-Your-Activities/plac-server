package com.plac.domain.plan.repository.bookmark;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.plan.entity.BookmarkPlan;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.repository.plan.PlanRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@Import({
        BookmarkPlanQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookmarkPlanRepositoryTest {

    @Autowired
    private BookmarkPlanRepository bookmarkPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Test
    @DisplayName("사용자 ID로 북마크 플랜들을 찾는다.")
    void getBookmarksByUserId() {
        // given
        User u = User.builder()
                .username("user1")
                .password("1234")
                .build();
        User user1 = userRepository.save(u);

        User u2 = User.builder()
                .username("user2")
                .password("1234")
                .build();
        User user2 = userRepository.save(u2);

        List<BookmarkPlan> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                    .user(user1)
                    .build();
            list.add(bookmarkPlan);
        }

        for (int i = 0; i < 10; i++) {
            BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                    .user(user2)
                    .build();
            list.add(bookmarkPlan);
        }
        bookmarkPlanRepository.saveAll(list);

        // when
        List<BookmarkPlan> result
                = bookmarkPlanRepository.findBookmarskPlansByUserId(user1.getId());
        List<BookmarkPlan> result2
                = bookmarkPlanRepository.findBookmarskPlansByUserId(user2.getId());

        boolean expressResult = result.stream()
                .anyMatch(bookmarkPlan -> bookmarkPlan.getUser().getId().equals(user1.getId()));

        boolean expressResult2 = result2.stream()
                .anyMatch(bookmarkPlan -> bookmarkPlan.getUser().getId().equals(user2.getId()));

        // then
        assertThat(result).hasSize(5);
        assertThat(result2).hasSize(10);
        assertTrue(expressResult);
        assertTrue(expressResult2);
    }

    @Test
    @DisplayName("사용자 ID, 플랜 ID로 북마크 플랜을 찾는다.")
    void getBookmarkByUserIdAndPlanId() {
        // given
        User u = User.builder()
                .username("user1")
                .password("1234")
                .build();
        User user = userRepository.save(u);

        Plan p = Plan.builder()
                .name("plan-first")
                .destinationName("test-dst")
                .build();
        Plan plan = planRepository.save(p);

        bookmarkPlanRepository.save(
                BookmarkPlan.builder()
                        .user(user)
                        .plan(plan)
                        .build()
        );

        // when
        BookmarkPlan result = bookmarkPlanRepository.findByUserIdAndPlanId(user.getId(), plan.getId())
                .orElseThrow();

        // then
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getPlan().getId()).isEqualTo(plan.getId());
        assertThat(result.getPlan()).isEqualTo(plan);
    }

    @Test
    @DisplayName("플랜 ID로 북마크 플랜들을 찾는다.")
    void getBookmarksByPlanId() {
        // given
        Plan p = Plan.builder()
                .name("plan-first")
                .destinationName("test-dst")
                .build();
        Plan plan = planRepository.save(p);

        List<BookmarkPlan> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                    .plan(plan)
                    .build();
            list.add(bookmarkPlan);
        }

        for (int i = 0; i < 10; i++) {
            Plan p2 = Plan.builder()
                    .name("plan-second")
                    .destinationName("test-dst")
                    .build();
            Plan plan2 = planRepository.save(p2);

            BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                    .plan(plan2)
                    .build();
            list.add(bookmarkPlan);
        }
        bookmarkPlanRepository.saveAll(list);

        // when
        List<BookmarkPlan> result
                = bookmarkPlanRepository.findBookmarksPlansByPlanId(plan.getId());

        boolean expressResult = result.stream()
                .anyMatch(bookmarkPlan -> bookmarkPlan.getPlan().getName().equals("plan-second"));

        // then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).getPlan()).isEqualTo(plan);
        assertFalse(expressResult);
    }
}