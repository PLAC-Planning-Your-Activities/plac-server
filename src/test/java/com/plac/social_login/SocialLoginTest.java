package com.plac.social_login;

import com.plac.social_login.domain.GoogleUserInfo;
import com.plac.social_login.domain.KakaoUserInfo;
import com.plac.social_login.domain.NaverUserInfo;
import com.plac.social_login.domain.Oauth2UserInfo;
import com.plac.user.exception.WeakPasswordException;
import com.plac.user.EmailNotifier;
import com.plac.user.MemoryUserRepository;
import com.plac.user.UserRepository;
import com.plac.user.WeakPasswordChecker;
import com.plac.social_login.service.Oauth2UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @DisplayName("소셜로그인 유저의 provider 값 확인")
    @Test
    void checkProvider(){
        Oauth2UserInfo naverUser = new NaverUserInfo();
        Oauth2UserInfo googleUser = new GoogleUserInfo();
        Oauth2UserInfo kakaoUser = new KakaoUserInfo();

        assertEquals("naver", naverUser.getProvider());
        assertEquals("google", googleUser.getProvider());
        assertEquals("kakao", kakaoUser.getProvider());
    }

}
