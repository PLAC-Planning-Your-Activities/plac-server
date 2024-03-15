package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.KakaoPlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.entity.PlaceDibs;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.place.repository.placeDibs.PlaceDibsRepository;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.service.UserService;
import com.plac.security.auth.CustomUserDetails;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
@TestMethodOrder(OrderAnnotation.class)
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

    private User user;

    @BeforeEach
    void setUp() {
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username("test-user")
                .password("passwd")
                .build();
        userService.signUp(userRequest);

        user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
    }

    @Test
    @Order(0)
    @DisplayName("마이리스트 장소 추가")
    void addMyListPlace() {
        // given
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        KakaoPlaceInfo info = KakaoPlaceInfo.builder()
                .kakaoPlaceId(9L)
                .build();

        // when
        placeService.triggerDibsMyListPlace(info);
        Place savedPlace = placeRepository.findByKakaoPlaceId(info.getKakaoPlaceId())
                .orElseThrow();
        PlaceDibs result = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(1L, info.getKakaoPlaceId())
                .orElseThrow();

        // then
        assertThat(savedPlace.getId()).isEqualTo(1L);
        assertThat(savedPlace.getKakaoPlaceId()).isEqualTo(9L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getKakaoPlaceId()).isEqualTo(9L);
    }

    @Test
    @Order(1)
    @DisplayName("마이리스트 장소 삭제")
    void deleteMyListPlace() {
        // given
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
                .userId(2L)
                .kakaoPlaceId(place.getKakaoPlaceId())
                .build();

        PlaceDibs placeDibs = placeDibsRepository.save(pb);

        // when
        PlaceDibs dibsResult = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(2L, place.getKakaoPlaceId())
                .orElseThrow();

        placeService.deleteMyListPlace(placeDibs.getKakaoPlaceId());

        Optional<PlaceDibs> result = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(2L, place.getKakaoPlaceId());

        // then
        assertThat(dibsResult.getId()).isEqualTo(2L);
        assertThat(dibsResult.getUserId()).isEqualTo(2L);
        assertThat(dibsResult.getKakaoPlaceId()).isEqualTo(11L);
        assertThat(result).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("찜 두번 시 장소 삭제")
    void checkDeletedMyListPlace() {
        // given
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        KakaoPlaceInfo info = KakaoPlaceInfo.builder()
                .kakaoPlaceId(13L)
                .build();

        Place p = Place.builder()
                .placeName("test-place")
                .kakaoPlaceId(info.getKakaoPlaceId())
                .build();
        placeRepository.save(p);

        PlaceDibs placeDibs = PlaceDibs.create(info.getKakaoPlaceId(), 3L);
        placeDibsRepository.save(placeDibs);

        // when
        placeService.triggerDibsMyListPlace(info);
        Optional<PlaceDibs> result = placeDibsRepository.findDibsByUserIdAndKakaoPlaceId(3L, 13L);

        // then
        assertThat(result).isEmpty();
    }

}