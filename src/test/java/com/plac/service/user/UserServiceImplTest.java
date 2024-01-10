package com.plac.service.user;

import com.plac.domain.user.dto.UserReqDto;
import com.plac.domain.user.dto.UserResDto;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.domain.user.service.PasswordChecker;
import com.plac.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordChecker passwordChecker;
    @MockBean
    private BCryptPasswordEncoder encoder;
    private User mockUser;
    private List<User> mockUserList;

    @BeforeEach
    void setUp(){
        Long userId = 1L;
        mockUser = User.builder()
                .id(userId)
                .username("test1@email.com")
                .provider("normal")
                .roles("ROLE_USER")
                .build();

        mockUserList = Arrays.asList(
                User.builder()
                        .username("test2@example.com")
                        .provider("google")
                        .roles("ROLE_USER")
                        .build(),
                User.builder()
                        .username("test3@example.com")
                        .provider("kakao")
                        .roles("ROLE_USER")
                        .build()
        );
    }

    @Test
    @DisplayName("사용자 등록(회원가입)이 올바르게 동작하는지 테스트")
    void signUpTest() {
        UserReqDto.CreateUser reqDto = new UserReqDto.CreateUser();
        reqDto.setUsername("test1@email.com");
        reqDto.setPassword("password1234");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordChecker.checkWeakPassword(anyString())).thenReturn(false);

        UserResDto result = userService.signUp(reqDto);

        assertNotNull(result);
        assertEquals(reqDto.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("유저 조회 테스트. 특정 사용자 ID로 사용자를 조회할 때, 예상대로 사용자가 반환되는지를 확인하기 위해 작성")
    void findUserTest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        User result = userService.findUser(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    @DisplayName("유저 삭제 테스트 : deleteUser() 호출 시 예외가 발생하지 않음을 확인")
    void deleteUserByIdTest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    @DisplayName("모든 유저 조회 테스트")
    void findAllUserTest() {
        when(userRepository.findAll()).thenReturn(mockUserList);

        List<UserResDto> result = userService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}