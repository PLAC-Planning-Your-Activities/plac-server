package com.plac.domain.social_login.provider.token;

import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.dto.SocialLoginRequest;
import com.plac.domain.social_login.service.token_info.GoogleTokenProvider;
import com.plac.domain.social_login.service.token_info.KakaoTokenProvider;
import com.plac.domain.social_login.service.token_info.NaverTokenProvider;
import com.plac.exception.common.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenProviderContext {
    private Map<String, TokenProviderStrategy> strategies;

    public TokenProviderContext(NaverTokenProvider naverTokenProvider,
                                GoogleTokenProvider googleTokenProvider,
                                KakaoTokenProvider kakaoTokenProvider) {
        this.strategies = new HashMap<>();
        strategies.put("naver", naverTokenProvider);
        strategies.put("google", googleTokenProvider);
        strategies.put("kakao", kakaoTokenProvider);
    }

    public Oauth2TokenResDto getTokenFromOauth2AuthServer(SocialLoginRequest socialLoginRequest){
        TokenProviderStrategy strategy = strategies.get(socialLoginRequest.getProvider().toLowerCase());

        if(strategy != null){
            return strategy.getToken(socialLoginRequest);
        }
        throw new BadRequestException("Unknown Provider: " + socialLoginRequest.getProvider());
    }
}
