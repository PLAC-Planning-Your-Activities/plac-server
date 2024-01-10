package com.plac.domain.social_login.provider.user_info;

import com.plac.domain.social_login.dto.Oauth2TokenResDto;

import java.util.Map;

public interface Oauth2UserInfoProviderStrategy {
    Map<String, Object> getUserInfo(Oauth2TokenResDto tokenResponse);
}
