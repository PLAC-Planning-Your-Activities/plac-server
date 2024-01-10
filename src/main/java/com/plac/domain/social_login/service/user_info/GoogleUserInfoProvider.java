package com.plac.domain.social_login.service.user_info;

import com.plac.config.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.provider.user_info.Oauth2UserInfoProviderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleUserInfoProvider implements Oauth2UserInfoProviderStrategy {

    private final GoogleOauth2Properties googleOauth2Properties;

    @Override
    public Map<String, Object> getUserInfo(Oauth2TokenResDto tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(googleOauth2Properties.getUserInfoEndpoint())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }
}
