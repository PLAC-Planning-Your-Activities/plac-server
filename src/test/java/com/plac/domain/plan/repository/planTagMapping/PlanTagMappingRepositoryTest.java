package com.plac.domain.plan.repository.planTagMapping;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.PlanTag;
import com.plac.domain.plan.entity.PlanTagMapping;
import com.plac.domain.plan.repository.plan.PlanRepository;
import com.plac.domain.plan.repository.planTag.PlanTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import({
        PlanTagMappingQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanTagMappingRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanTagRepository planTagRepository;

    @Autowired
    private PlanTagMappingRepository planTagMappingRepository;

    @Test
    @DisplayName("플랜 ID로 태그들을 조회한다.")
    void getTagsByPlanId() {
        // given
        Plan p = Plan.builder()
                .name("plan")
                .destinationName("test-dst")
                .build();
        Plan plan = planRepository.save(p);

        PlanTag t = PlanTag.builder()
                .tagName("test-tag")
                .build();
        PlanTag tag = planTagRepository.save(t);

        for (int i = 0; i < 3; i++) {
            PlanTagMapping ptm = PlanTagMapping.builder()
                    .plan(plan)
                    .planTag(tag)
                    .build();
            planTagMappingRepository.save(ptm);
        }

        // when
        List<PlanTagMapping> result =
                planTagMappingRepository.findTagsByPlanId(plan.getId());

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getPlanTag().getId()).isEqualTo(1L);
        assertThat(result.get(0).getPlan().getId()).isEqualTo(1L);
    }
}