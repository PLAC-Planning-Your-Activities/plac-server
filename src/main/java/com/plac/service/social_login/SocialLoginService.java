package com.plac.service.social_login;

import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.SocialLoginResDto;

public interface SocialLoginService {
    SocialLoginResDto signIn(SocialLoginReqDto.Login req);
}
