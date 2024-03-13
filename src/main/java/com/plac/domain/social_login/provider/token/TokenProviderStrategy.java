package com.plac.domain.social_login.provider.token;

import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.dto.SocialLoginRequest;

public interface TokenProviderStrategy {
    Oauth2TokenResDto getToken(SocialLoginRequest request);
}
