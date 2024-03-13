package com.plac.domain.social_login.service.token_info;

import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.dto.SocialLoginRequest;
import com.plac.domain.social_login.provider.token.TokenProviderStrategy;
import com.plac.domain.social_login.service.user_info.GoogleOauth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GoogleTokenProvider implements TokenProviderStrategy {

    private final GoogleOauth2Properties googleOauth2Properties;

    @Override
    public Oauth2TokenResDto getToken(SocialLoginRequest socialLoginRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        String code = URLDecoder.decode(socialLoginRequest.getCode(), StandardCharsets.UTF_8);

        formData.add("code", code);
        formData.add("client_id", googleOauth2Properties.getClientId());
        formData.add("client_secret", googleOauth2Properties.getClientSecret());
        formData.add("redirect_uri", googleOauth2Properties.getRedirectUri());
        formData.add("grant_type", googleOauth2Properties.getGrantType());

        // POST 요청 보내기
        return WebClient.create().
                post()
                .uri(googleOauth2Properties.getTokenUri())
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Oauth2TokenResDto.class)
                .block();
    }
}
