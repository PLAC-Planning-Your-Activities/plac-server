package com.plac.domain.plan.repository.plan;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.plan.entity.Plan;
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

@ActiveProfiles("test")
@DataJpaTest
@Import({
        PlanQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Test
    @DisplayName("목적지 이름으로 플랜들을 찾는다.")
    void getPlans() {
        //given
        List<Plan> plans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Plan plan = Plan.builder()
                    .name("plan" + i)
                    .destinationName("test-dst")
                    .build();
            plans.add(plan);
        }

        Plan other = Plan.builder()
                .name("plan9")
                .destinationName("the other place")
                .build();
        plans.add(other);
        planRepository.saveAll(plans);

        // when
        List<Plan> result = planRepository.findPlansByDestinationName("test-dst");

        // then
        assertThat(result).hasSize(10);
        assertThat(result).doesNotContain(other);
    }
}