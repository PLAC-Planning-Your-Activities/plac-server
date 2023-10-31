package com.plac.user;

import com.plac.user.exception.WeakPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRegisterTest {

    private UserService userService;

    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);

    @BeforeEach
    void setUp(){
        userService = new UserService(mockPasswordChecker);
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

}
