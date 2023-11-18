package com.plac.service.social_login.provider.token;

import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;

public interface TokenProviderStrategy {
    Oauth2TokenResDto getToken(SocialLoginReqDto.Login req);
}
