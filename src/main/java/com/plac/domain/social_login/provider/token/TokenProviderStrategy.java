package com.plac.domain.social_login.provider.token;

import com.plac.domain.social_login.dto.SocialLoginReqDto;
import com.plac.domain.social_login.dto.Oauth2TokenResDto;

public interface TokenProviderStrategy {
    Oauth2TokenResDto getToken(SocialLoginReqDto.Login req);
}
