package com.plac.domain.social_login.provider.token;

import com.plac.config.dto.SocialLoginReqDto;
import com.plac.config.dto.Oauth2TokenResDto;

public interface TokenProviderStrategy {
    Oauth2TokenResDto getToken(SocialLoginReqDto.Login req);
}
