package com.plac.social_login;

import com.plac.social_login.domain.Oauth2UserInfo;
import com.plac.social_login.exception.WeakPasswordException;
import com.plac.social_login.repository.EmailNotifier;
import com.plac.social_login.repository.MemoryUserRepository;
import com.plac.social_login.repository.UserRepository;
import com.plac.social_login.repository.WeakPasswordChecker;
import com.plac.social_login.service.Oauth2UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SocialLoginTest {

    private Oauth2UserInfo oAuth2UserInfo;

    private Oauth2UserService oauth2UserService;

    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);
    private UserRepository fakeRepository = new MemoryUserRepository();
    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    void setUp(){
        oauth2UserService = new Oauth2UserService(mockPasswordChecker,
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
            oauth2UserService.register("username1@email.com", "pw");
        });
    }

    @DisplayName("회원가입 시,패스워드 체커에서 암호검사 수행")
    @Test
    void checkPassword(){
        oauth2UserService.register("username1@naver.com", "pw123");

        BDDMockito.then(mockPasswordChecker)
                .should()
                .checkWeakPassword(BDDMockito.anyString());
    }

}
