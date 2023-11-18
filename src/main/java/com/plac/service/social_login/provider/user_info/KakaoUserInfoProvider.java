package com.plac.service.social_login.provider.user_info;

import com.plac.dto.response.social_login.Oauth2TokenResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class KakaoUserInfoProvider implements Oauth2UserInfoProviderStrategy {

    private ClientRegistration clientRegistration;
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public KakaoUserInfoProvider(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
    }

    @Override
    public Map<String, Object> getUserInfo(Oauth2TokenResDto tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }
}
