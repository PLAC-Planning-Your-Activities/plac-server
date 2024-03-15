package com.plac.domain.plan.service;

import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.repository.DestinationRepository;
import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.entity.PlanPlaceMapping;
import com.plac.domain.plan.repository.planPlaceMapping.PlanPlaceMappingMappingRepository;
import com.plac.domain.plan.repository.plan.PlanRepository;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.domain.user.service.PasswordChecker;
import com.plac.domain.user.service.UserService;
import com.plac.security.auth.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlanServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanPlaceMappingMappingRepository planPlaceMappingRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private PasswordChecker passwordChecker;

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;


    @Test
    @DisplayName("플랜 생성한다.")
    void createPlan() {
        // given
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username("test-user")
                .password("passwd")
                .build();

        userService.signUp(userRequest);
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        List<PlaceInfo> list = new ArrayList<>();
        PlaceInfo info = PlaceInfo.builder()
                .placeName("test-place-name")
                .kakaoPlaceId(1L)
                .build();
        list.add(info);

        PlanCreateRequest request = PlanCreateRequest.builder()
                .planName("test")
                .placeList(list)
                .destinationName("test-dst")
                .build();

        PlanCreateResponse created = planService.createPlan(request);

        // when
        Place place = placeRepository.findByKakaoPlaceId(1L).orElseThrow();
        Destination destination = destinationRepository.findByName("test-dst").orElseThrow();
        List<PlanPlaceMapping> planPlaces = planPlaceMappingRepository.findByPlanId(created.getPlanId());

        // then
        assertThat(place.getPlaceName()).isEqualTo("test-place-name");
        assertThat(place.getKakaoPlaceId()).isEqualTo(1L);
        assertThat(destination.getName()).isEqualTo("test-dst");
        assertThat(planPlaces).hasSize(1);
        assertThat(planPlaces.get(0).getPlan().getName()).isEqualTo("test");
        assertThat(planPlaces.get(0).getPlace().getPlaceName()).isEqualTo("test-place-name");
    }
}