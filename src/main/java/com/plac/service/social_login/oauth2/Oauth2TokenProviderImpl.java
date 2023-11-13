package com.plac.service.social_login.oauth2;

import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
@Slf4j
public class Oauth2TokenProviderImpl implements Oauth2TokenProvider{

    @Override
    public Oauth2TokenResDto getTokenFromNaver(SocialLoginReqDto.Login req, ClientRegistration provider) {
        // FormData 설정
        MultiValueMap<String, String> formData = createNaverFormData(req, provider);

        // WebClient 요청 구성 및 실행
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Oauth2TokenResDto.class)
                .block();

    }

    @Override
    public Oauth2TokenResDto getTokenFromGoogle(SocialLoginReqDto.Login req, ClientRegistration provider) {
        MultiValueMap<String, String> formData = createGoogleFormData(req, provider);

        log.info("client_id={}", provider.getClientId());
        log.info("client_secret={}", provider.getClientSecret());
        log.info("code={}", req.getCode());
        log.info("redirect_uri={}", provider.getRedirectUri());

        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Oauth2TokenResDto.class)
                .block();
    }

    @Override
    public Oauth2TokenResDto getTokenFromKakao(SocialLoginReqDto.Login req, ClientRegistration provider) {
        // FormData 설정
        MultiValueMap<String, String> formData = createKakaoFormData(req, provider);

        // WebClient 요청 구성 및 실행
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Oauth2TokenResDto.class)
                .block();
    }

    private static MultiValueMap<String, String> createNaverFormData(SocialLoginReqDto.Login req, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("code", req.getCode());
        //formData.add("state", 1234);
        return formData;
    }

    private static MultiValueMap<String, String> createGoogleFormData(SocialLoginReqDto.Login req, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("code", req.getCode());
        formData.add("redirect_uri", provider.getRedirectUri());
        return formData;
    }

    private static MultiValueMap<String, String> createKakaoFormData(SocialLoginReqDto.Login req, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("code", req.getCode());
        return formData;
    }



}
