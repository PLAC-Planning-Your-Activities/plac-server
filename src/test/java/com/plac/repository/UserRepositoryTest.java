package com.plac.repository;

import com.plac.domain.user.entity.User;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        Long userId = 1L;

        User buildUser = User.builder()
                .id(userId)
                .provider("normal")
                .username("test1@email.com")
                .password("password1234")
                .roles("ROLE_USER")
                .build();
        userRepository.save(buildUser);
    }

    @DisplayName("유저 저장 테스트")
    @Test
    void userSaveTest(){
        User testUser = User.builder()
                .provider("google")
                .username("test2@email.com")
                .password("password1234")
                .roles("ROLE_USER")
                .build();
        userRepository.save(testUser);

        Optional<User> userOpt = userRepository.findByUsername("test2@email.com");
        assertEquals(userOpt.get(), testUser);
    }

    @DisplayName("user_id로 해당 id를 가지는 유저 조회")
    @Test
    void findByIdTest(){
        Optional<User> userOpt = userRepository.findByUsernameAndProvider("test1@email.com", "normal");

        Optional<User> result = userRepository.findById(userOpt.get().getId());
        assertEquals(userOpt.get().getId(), result.get().getId());
    }

    @DisplayName("username, provider 로 유저를 찾을 수 있음")
    @Test
    void findUsernameAndProviderTest(){
        Optional<User> userOpt = userRepository.findByUsernameAndProvider("test1@email.com", "normal");
        assertEquals("test1@email.com", userOpt.get().getUsername());
        assertEquals("normal", userOpt.get().getProvider());
    }

    @DisplayName("유저 삭제 테스트")
    @Test
    void deleteTest(){
        User testUser = User.builder()
                .provider("naver")
                .username("naver_user@email.com")
                .password("password1234")
                .roles("ROLE_USER")
                .build();
        userRepository.save(testUser);

        Optional<User> userOpt = userRepository.findByUsernameAndProvider("naver_user@email.com", "naver");
        // 사용자가 존재하는 경우에만 테스트 진행
        assertTrue(userOpt.isPresent(), "유저가 존재합니다.");

        userRepository.delete(userOpt.get());

        Optional<User> deleteUser = userRepository.findByUsernameAndProvider("naver_user", "naver");
        assertFalse(deleteUser.isPresent(), "사용자가 존재하지 않습니다.");

    }

}
