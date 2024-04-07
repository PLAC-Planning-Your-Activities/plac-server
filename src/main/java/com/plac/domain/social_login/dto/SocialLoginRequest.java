package com.plac.domain.social_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SocialLoginRequest {
    private String provider;
    private String code;
}
