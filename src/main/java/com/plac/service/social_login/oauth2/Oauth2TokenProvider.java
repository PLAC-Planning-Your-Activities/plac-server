package com.plac.service.social_login.oauth2;

import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface Oauth2TokenProvider {

    Oauth2TokenResDto getTokenFromNaver(SocialLoginReqDto.Login req, ClientRegistration provider);
    Oauth2TokenResDto getTokenFromGoogle(SocialLoginReqDto.Login req, ClientRegistration provider);
    Oauth2TokenResDto getTokenFromKakao(SocialLoginReqDto.Login req, ClientRegistration provider);

}
