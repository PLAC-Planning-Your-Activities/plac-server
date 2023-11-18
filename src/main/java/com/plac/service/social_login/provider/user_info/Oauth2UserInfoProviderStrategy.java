package com.plac.service.social_login.provider.user_info;

import com.plac.dto.response.social_login.Oauth2TokenResDto;

import java.util.Map;

public interface Oauth2UserInfoProviderStrategy {
    Map<String, Object> getUserInfo(Oauth2TokenResDto tokenResponse);
}
