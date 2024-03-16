package com.plac.domain.plan.repository.planDibs;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.PlanDibs;
import com.plac.domain.plan.repository.plan.PlanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import({
        PlanDibsQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanDibsQueryRepositoryImplTest {

    @Autowired
    private PlanDibsRepository planDibsRepository;

    @Autowired
    private PlanRepository planRepository;

    @Test
    @Order(0)
    @DisplayName("마이리스트에 플랜 찜한다.")
    void addMyListPlan() {
        // given
        Plan p = Plan.builder()
                .name("test-plan-name")
                .destinationName("test-plan-destinationName")
                .build();

        Plan plan = planRepository.save(p);

        long userId = 1L;
        PlanDibs planDibs = PlanDibs.create(plan.getId(), userId);

        // when
        PlanDibs result = planDibsRepository.save(planDibs);

        // then
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    @Order(1)
    @DisplayName("마이리스트에 찜 플랜 가져온다.")
    void getMyListPlans() {
        // given
        List<Plan> plans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Plan plan = Plan.builder()
                    .name("test-place-name " + (i + 1))
                    .destinationName("test-destinationName")
                    .build();
            plans.add(plan);
        }
        planRepository.saveAll(plans);

        long userId = 2L;
        long userId2 = 3L;

        List<PlanDibs> user1Dibs = new ArrayList<>();
        for (int i = 0; i < plans.size(); i++) {
            long planId = (long) (i + 2);
            PlanDibs planDibs = PlanDibs.create(planId, userId);
            user1Dibs.add(planDibs);
        }
        planDibsRepository.saveAll(user1Dibs);

        List<PlanDibs> user2Dibs = new ArrayList<>();
        for (int i = 2; i < 6; i++) {
            long planId = (long) (i + 2);
            PlanDibs planDibs = PlanDibs.create(planId, userId2);
            user2Dibs.add(planDibs);
        }
        planDibsRepository.saveAll(user2Dibs);

        // when
        List<Plan> userId1Dibs = planRepository.findPlanDibsByUserId(userId);
        List<Plan> userId2Dibs = planRepository.findPlanDibsByUserId(userId2);

        // then
        assertThat(userId1Dibs).hasSize(10);
        assertThat(userId2Dibs).hasSize(4);
    }
}