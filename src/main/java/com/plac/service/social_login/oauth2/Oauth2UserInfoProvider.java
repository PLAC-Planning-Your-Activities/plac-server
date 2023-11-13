package com.plac.service.social_login.oauth2;

import com.plac.domain.social_login.Oauth2UserInfo;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Map;

public interface Oauth2UserInfoProvider {
    Map<String, Object> getUserInfoFromAuthServer(ClientRegistration provider, String providerName, Oauth2TokenResDto tokenResponse);
    Oauth2UserInfo makeOauth2UserInfo(String providerName, Map<String, Object> userAttributes);
}