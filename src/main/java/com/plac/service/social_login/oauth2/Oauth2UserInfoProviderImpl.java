package com.plac.service.social_login.oauth2;

import com.plac.domain.social_login.GoogleUserInfo;
import com.plac.domain.social_login.KakaoUserInfo;
import com.plac.domain.social_login.NaverUserInfo;
import com.plac.domain.social_login.Oauth2UserInfo;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import com.plac.exception.social_login.ProviderNotSupportedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class Oauth2UserInfoProviderImpl implements Oauth2UserInfoProvider{

    @Override
    public Map<String, Object> getUserInfoFromAuthServer(ClientRegistration provider, String providerName, Oauth2TokenResDto tokenResponse) {
        if (providerName.equals("naver")){
            return getNaverUserInfo(provider, tokenResponse);
        } else if (providerName.equals("google")){
            return getGoogleUserInfo(provider, tokenResponse);
        } else if (providerName.equals("kakao")){
            return getKakaoUserInfo(provider, tokenResponse);
        }
        throw new ProviderNotSupportedException("해당 회사의 소셜 로그인을 지원하지 않습니다. 다른 값을 입력해주세요");
    }

    @Override
    public Oauth2UserInfo makeOauth2UserInfo(String providerName, Map<String, Object> userAttributes) {
        if (providerName.equals("naver")){
            return new NaverUserInfo(userAttributes);
        }else if(providerName.equals("google")){
            return new GoogleUserInfo(userAttributes);
        }else if(providerName.equals("kakao")){
            return new KakaoUserInfo(userAttributes);
        }
        throw new ProviderNotSupportedException("해당 회사의 소셜 로그인을 지원하지 않습니다. 다른 값을 입력해주세요");
    }


    private Map<String, Object> getGoogleUserInfo(ClientRegistration provider, Oauth2TokenResDto tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }

    private Map<String, Object> getNaverUserInfo(ClientRegistration provider, Oauth2TokenResDto tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }

    private Map<String, Object> getKakaoUserInfo(ClientRegistration provider, Oauth2TokenResDto tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }
}
