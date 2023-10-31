package com.plac.user;

import com.plac.user.exception.DuplUsernameException;
import com.plac.user.exception.WeakPasswordException;
import com.plac.user.repository.MemoryUserRepository;
import com.plac.user.repository.UserRepository;
import com.plac.user.service.SpyEmailNotifier;
import com.plac.user.service.WeakPasswordChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegisterTest {

    private UserService userService;

    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);
    private UserRepository fakeRepository = new MemoryUserRepository();

    private SpyEmailNotifier spyEmailNotifier = new SpyEmailNotifier();


    @BeforeEach
    void setUp(){
        userService = new UserService(mockPasswordChecker,
                fakeRepository, spyEmailNotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword() {
        BDDMockito.given(mockPasswordChecker.checkWeakPassword("weak-pw"))
                .willReturn(true);

        assertThrows(WeakPasswordException.class, () -> {
            userService.register("username1@email.com", "weak-pw");
        });
    }

    @DisplayName("username(이메일)이 이미 존재하는 경우, 회원가입 불가")
    @Test
    void username이_이미_존재하는경우_회원가입_불가(){
        fakeRepository.save(new User("username1@email.com", "pw123"));

        assertThrows(DuplUsernameException.class, ()->{
            userService.register("username1@email.com", "pw334");
        });
    }

    @DisplayName("가입하면 해당 이메일로 메일 전송했는지, 확인")
    @Test
    void afterRegisterSendMail(){
        userService.register("username1@email.com", "pw1");

        assertTrue(spyEmailNotifier.isCalled());
        assertEquals(
                "username1@email.com",
                spyEmailNotifier.getEmail()
        );
    }

}
