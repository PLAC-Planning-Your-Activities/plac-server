package com.plac.service.social_login;

import com.plac.domain.user.entity.User;
import com.plac.domain.social_login.GoogleUserInfo;
import com.plac.domain.social_login.Oauth2UserInfo;
import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import com.plac.dto.response.social_login.SocialLoginResDto;
import com.plac.domain.user.repository.UserRepository;
import com.plac.service.social_login.provider.token.TokenProviderContext;
import com.plac.service.social_login.provider.user_info.Oauth2UserInfoContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SocialLoginServiceImplTest {

    @InjectMocks
    private SpySocialLoginServiceImpl spySocialLoginService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProviderContext tokenProviderContext;
    @Mock
    private Oauth2UserInfoContext oauth2UserInfoContext;

    private SocialLoginReqDto.Login req;
    private Oauth2TokenResDto mockTokenResponse;
    private Map<String, Object> mockUserInfo;
    private Oauth2UserInfo oauth2UserInfo;
    private User mockUser;

    @BeforeEach
    void setUp(){
        req = new SocialLoginReqDto.Login("google", "codeNum123");
        mockTokenResponse = createMockTokenResponse();
        mockUserInfo = createMockUserInfo();
        oauth2UserInfo = new GoogleUserInfo(mockUserInfo);
        mockUser = createUser();

        when(tokenProviderContext.getTokenFromOauth2AuthServer(any(SocialLoginReqDto.Login.class))).thenReturn(mockTokenResponse);
        when(oauth2UserInfoContext.getUserInfoFromAuthServer(anyString(), any(Oauth2TokenResDto.class))).thenReturn(mockUserInfo);
        when(oauth2UserInfoContext.makeOauth2UserInfo(anyString(), any(Map.class))).thenReturn(oauth2UserInfo);
        when(userRepository.findByUsernameAndProvider(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
    }

    @Test
    @DisplayName("sigIn 실행 시 내부 메서드 실행 검증")
    void signIn_실행시_내부메서드가_전부_실행되어야함(){
        spySocialLoginService.signIn(req);

        BDDMockito.then(tokenProviderContext)
                .should()
                .getTokenFromOauth2AuthServer(any(SocialLoginReqDto.Login.class));

        BDDMockito.then(oauth2UserInfoContext)
                .should()
                .getUserInfoFromAuthServer(anyString(), any(Oauth2TokenResDto.class));

        BDDMockito.then(oauth2UserInfoContext)
                .should()
                .makeOauth2UserInfo(anyString(), any(Map.class));
    }

    @Test
    @DisplayName("소셜 로그인 시도(sign-In) 테스트")
    void signIn_실행_후_결과검증() {
        SocialLoginResDto result = spySocialLoginService.signIn(req);
        assertNotNull(result);

        // 결과 검증
        assertEquals("Bearer", result.getToken_type(), "토큰 타입은 Bearer.");
        assertNotNull(result.getAccess_token());

        User returnedUser = result.getUser();
        assertNotNull(returnedUser);
        assertEquals(mockUser.getUsername(), returnedUser.getUsername());
        assertEquals(mockUser.getProvider(), returnedUser.getProvider());
    }

    private static User createUser() {
        return User.builder()
                .provider("google")
                .username("user1@email.com")
                .build();
    }

    private static Map<String, Object> createMockUserInfo() {
        return Map.of(
                "email", "user1@email.com",
                "picture", "http://example.com/user1.jpg",
                "provider", "google"
        );
    }

    private static Oauth2TokenResDto createMockTokenResponse() {
        return Oauth2TokenResDto.builder()
                .access_token("access_token_ABC")
                .refresh_token("refresh_token_ABC")
                .build();
    }

}