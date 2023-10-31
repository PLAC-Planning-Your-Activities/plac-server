package com.plac.social_login;

import com.plac.social_login.domain.OAuth2UserInfo;
import com.plac.social_login.exception.WeakPasswordException;
import com.plac.social_login.repository.EmailNotifier;
import com.plac.social_login.repository.MemoryUserRepository;
import com.plac.social_login.repository.UserRepository;
import com.plac.social_login.repository.WeakPasswordChecker;
import com.plac.social_login.service.SocialLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SocialLoginTest {

    private OAuth2UserInfo oAuth2UserInfo;

    private SocialLoginService socialLoginService;

    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);
    private UserRepository fakeRepository = new MemoryUserRepository();
    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    void setUp(){
        socialLoginService = new SocialLoginService(mockPasswordChecker,
                fakeRepository,
                mockEmailNotifier
                );
    }

    @DisplayName("약한 암호면 회원가입 실패")
    @Test
    void weakPassword_then_fail(){
        BDDMockito.given(mockPasswordChecker.checkWeakPassword("pw"))
                .willReturn(true);
        assertThrows(WeakPasswordException.class, ()->{
            socialLoginService.register("username1@email.com", "pw");
        });
    }

}
