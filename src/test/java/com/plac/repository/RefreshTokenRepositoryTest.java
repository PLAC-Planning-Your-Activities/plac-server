package com.plac.repository;

import com.plac.domain.user.entity.RefreshToken;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RefreshTokenRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private User buildUser;

    @BeforeEach
    void setUp(){
        settings("test33@email.com", "normal");
    }

    @Test
    @DisplayName("유저id로 리프레시토큰 반환")
    void findByUserId() {
        Optional<User> userOpt = userRepository.findByUsernameAndProvider("test33@email.com", "normal");
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(userOpt.get().getId());

        assertNotNull(refreshTokenOpt);
    }

    @Test
    @DisplayName("id로 리프레시토큰 반환")
    void findById() {
        Optional<User> userOpt = userRepository.findByUsernameAndProvider("test33@email.com", "normal");
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(userOpt.get().getId());
        UUID refreshTokenId = refreshTokenOpt.get().getId();

        assertEquals(refreshTokenOpt.get(), refreshTokenRepository.findById(refreshTokenId).get());
    }

    @Test
    @DisplayName("리프레시 토큰 삭제")
    void delete(){
        settings("test123@email.com", "google");
        Optional<User> userOpt = userRepository.findByUsernameAndProvider("test123@email.com", "google");
        Long userId = userOpt.get().getId();
        Optional<RefreshToken> refreshTokenOpt= refreshTokenRepository.findByUserId(userId);

        refreshTokenRepository.delete(refreshTokenOpt.get());
        assertFalse(refreshTokenRepository.findByUserId(userId).isPresent());
    }

    private void settings(String username, String provider) {
        buildUser = User.builder()
                .provider(provider)
                .username(username)
                .password("password1234")
                .roles("ROLE_USER")
                .build();
        userRepository.save(buildUser);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID())
                .refreshToken("refreshTokenValue1235")
                .userId(buildUser.getId())
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}