package com.plac.domain.social_login.service.token_info;

import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.dto.SocialLoginRequest;
import com.plac.domain.social_login.provider.token.TokenProviderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class KakaoTokenProvider implements TokenProviderStrategy {
    ClientRegistration clientRegistration;
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public KakaoTokenProvider(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
    }

    @Override
    public Oauth2TokenResDto getToken(SocialLoginRequest socialLoginRequest) {
        MultiValueMap<String, String> formData = createKakaoFormData(socialLoginRequest);

        return WebClient.create()
                .post()
                .uri(clientRegistration.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Oauth2TokenResDto.class)
                .block();
    }

    private MultiValueMap<String, String> createKakaoFormData(SocialLoginRequest socialLoginRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientRegistration.getClientId());
        formData.add("client_secret", clientRegistration.getClientSecret());
        formData.add("redirect_uri", clientRegistration.getRedirectUri());
        formData.add("code", socialLoginRequest.getCode());
        return formData;
    }
}