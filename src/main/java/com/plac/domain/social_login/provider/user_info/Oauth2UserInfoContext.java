package com.plac.domain.social_login.provider.user_info;

import com.plac.domain.social_login.entity.GoogleUserInfo;
import com.plac.domain.social_login.entity.KakaoUserInfo;
import com.plac.domain.social_login.entity.NaverUserInfo;
import com.plac.domain.social_login.entity.Oauth2UserInfo;
import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.service.user_info.GoogleUserInfoProvider;
import com.plac.domain.social_login.service.user_info.KakaoUserInfoProvider;
import com.plac.domain.social_login.service.user_info.NaverUserInfoProvider;
import com.plac.exception.common.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class Oauth2UserInfoContext {
    private Map<String, Oauth2UserInfoProviderStrategy> strategies;
    private Map<String, Function<Map<String, Object>, Oauth2UserInfo>> userInfoProviderMap = new HashMap<>();

    public Oauth2UserInfoContext(NaverUserInfoProvider naverUserInfoProvider,
                                 GoogleUserInfoProvider googleUserInfoProvider,
                                 KakaoUserInfoProvider kakaoUserInfoProvider) {
        this.strategies = new HashMap<>();
        strategies.put("naver", naverUserInfoProvider);
        strategies.put("google", googleUserInfoProvider);
        strategies.put("kakao", kakaoUserInfoProvider);

        userInfoProviderMap.put("naver", NaverUserInfo::new);
        userInfoProviderMap.put("google", GoogleUserInfo::new);
        userInfoProviderMap.put("kakao", KakaoUserInfo::new);
    }

    public Map<String, Object> getUserInfoFromAuthServer(String provider, Oauth2TokenResDto tokenResponse){
        Oauth2UserInfoProviderStrategy strategy = strategies.get(provider);
        if(strategy != null){
            Map<String, Object> userInfo = strategy.getUserInfo(tokenResponse);
            return userInfo;
        }
        throw new BadRequestException("Unknown Provider: " + provider);
    }

    public Oauth2UserInfo makeOauth2UserInfo(String provider, Map<String, Object> userAttributes) {
        Function<Map<String, Object>, Oauth2UserInfo> userInfoProvider = userInfoProviderMap.get(provider);

        if (userInfoProvider != null) {
            return userInfoProvider.apply(userAttributes);
        } else {
            throw new BadRequestException("Unknown Provider: " + provider);
        }
    }
}
