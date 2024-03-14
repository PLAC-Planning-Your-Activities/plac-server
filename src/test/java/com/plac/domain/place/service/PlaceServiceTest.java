package com.plac.domain.place.service;

import com.plac.domain.place.entity.Place;
import com.plac.domain.place.entity.PlaceDibs;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.place.repository.placeDibs.PlaceDibsRepository;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.entity.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceDibsRepository placeDibsRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("마이리스트 장소 삭제")
    void deleteMyListPlace() {
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

        Place p = Place.builder()
                .placeName("test-place")
                .kakaoPlaceId(11L)
                .build();
        Place place = placeRepository.save(p);

        PlaceDibs pb = PlaceDibs.builder()
                .userId(1L)
                .kakaoPlaceId(place.getKakaoPlaceId())
                .build();

        PlaceDibs placeDibs = placeDibsRepository.save(pb);

        // when
        PlaceDibs dibsResult = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(1L, place.getKakaoPlaceId())
                .orElseThrow();

        placeService.deleteMyListPlace(placeDibs.getKakaoPlaceId());

        Optional<PlaceDibs> result = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(1L, place.getKakaoPlaceId());

        // then
        assertThat(dibsResult.getId()).isEqualTo(1L);
        assertThat(dibsResult.getUserId()).isEqualTo(1L);
        assertThat(dibsResult.getKakaoPlaceId()).isEqualTo(11L);
        assertThat(result).isEmpty();
    }

}