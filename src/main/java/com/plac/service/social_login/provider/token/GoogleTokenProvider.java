package com.plac.service.social_login.provider.token;

import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import com.plac.service.social_login.GoogleOauth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GoogleTokenProvider implements TokenProviderStrategy {

    private final GoogleOauth2Properties googleOauth2Properties;

    @Override
    public Oauth2TokenResDto getToken(SocialLoginReqDto.Login req) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("code", req.getCode());
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
