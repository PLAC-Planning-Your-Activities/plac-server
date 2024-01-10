package com.plac.domain.social_login.provider.token;

import com.plac.domain.social_login.dto.SocialLoginReqDto;
import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.service.token_info.GoogleTokenProvider;
import com.plac.domain.social_login.service.token_info.KakaoTokenProvider;
import com.plac.domain.social_login.service.token_info.NaverTokenProvider;
import com.plac.exception.social_login.ProviderNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenProviderContext {
    private Map<String, TokenProviderStrategy> strategies;

    @Autowired
    public TokenProviderContext(NaverTokenProvider naverTokenProvider,
                                GoogleTokenProvider googleTokenProvider,
                                KakaoTokenProvider kakaoTokenProvider) {
        this.strategies = new HashMap<>();
        strategies.put("naver", naverTokenProvider);
        strategies.put("google", googleTokenProvider);
        strategies.put("kakao", kakaoTokenProvider);
    }

    public Oauth2TokenResDto getTokenFromOauth2AuthServer(SocialLoginReqDto.Login req){
        TokenProviderStrategy strategy = strategies.get(req.getProvider().toLowerCase());
        if(strategy != null){
            return strategy.getToken(req);
        }
        throw new ProviderNotSupportedException("Unknown Provider: " + req.getProvider());
    }
}
