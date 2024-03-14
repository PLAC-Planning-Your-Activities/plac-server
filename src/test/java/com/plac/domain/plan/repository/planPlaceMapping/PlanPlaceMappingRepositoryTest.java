package com.plac.domain.plan.repository.planPlaceMapping;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.PlanPlaceMapping;
import com.plac.domain.plan.repository.plan.PlanRepository;
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
        PlanPlaceQueryMappingRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanPlaceMappingRepositoryTest {

    @Autowired
    private PlanPlaceMappingMappingRepository planPlaceMappingRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    @DisplayName("플랜 아이디로 장소 조회")
    void getPlaces() {
        // given
        Plan plan = Plan.builder()
                .name("plan")
                .destinationName("test-dst")
                .build();
        Plan createdPlan = planRepository.save(plan);

        for (int i = 0 ; i < 3; i++) {
            Place place = Place.builder()
                    .placeName("place" + i)
                    .build();

            Place createdPlace = placeRepository.save(place);

            PlanPlaceMapping planPlace = PlanPlaceMapping.builder()
                    .plan(createdPlan)
                    .place(createdPlace)
                    .build();

            planPlaceMappingRepository.save(planPlace);
        }

        // when
        List<PlanPlaceMapping> result = planPlaceMappingRepository.findByPlanId(plan.getId());

        // then
        assertThat(result.size()).isEqualTo(3);
    }
}