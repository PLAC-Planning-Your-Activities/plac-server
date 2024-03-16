package com.plac.domain.plan.repository.planDibs;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.PlanDibs;
import com.plac.domain.plan.repository.plan.PlanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import({
        PlanDibsQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanDibsQueryRepositoryImplTest {

    @Autowired
    private PlanDibsRepository planDibsRepository;

    @Autowired
    private PlanRepository planRepository;

    @Test
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
}