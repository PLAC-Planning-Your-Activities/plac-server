package com.plac.repository;

import com.plac.domain.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
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
        User buildUser = User.builder()
                .provider("normal")
                .username("email1@email.com")
                .password("pw1234")
                .roles("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(buildUser);
    }

    @DisplayName("유저 저장 테스트")
    @Test
    void userSaveTest(){
        User buildUser = User.builder()
                .provider("normal")
                .username("email2@email.com")
                .password("pw1234")
                .roles("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(buildUser);

        Optional<User> userOpt = userRepository.findByUsername("email2@email.com");
        assertEquals(userOpt.get(), buildUser);
    }

    @DisplayName("user_id로 찾기 테스트")
    @Test
    void findByIdTest(){
        Optional<User> userOpt = userRepository.findById(1L);
        assertEquals("email1@email.com", userOpt.get().getUsername());
    }

    @DisplayName("username, provider 로 찾는 테스트")
    @Test
    void findUsernameAndProviderTest(){
        User buildUser = User.builder()
                .provider("naver")
                .username("email3@email.com")
                .password("pw12345")
                .roles("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(buildUser);

        Optional<User> userOpt = userRepository.findByUsernameAndProvider("email3@email.com", "naver");
        assertEquals("email3@email.com", userOpt.get().getUsername());
    }

    @DisplayName("유저 삭제 테스트")
    @Test
    void deleteTest(){
        // 기존에 저장된 사용자를 조회
        Optional<User> userOpt = userRepository.findById(1L);

        // 사용자가 존재하는 경우에만 테스트 진행
        assertTrue(userOpt.isPresent(), "유저가 존재합니다.");

        userRepository.delete(userOpt.get());

        Optional<User> deleteUser = userRepository.findById(1L);
        assertFalse(deleteUser.isPresent(), "사용자가 존재하지 않습니다.");

    }

}
