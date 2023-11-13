package com.plac.dto.request.social_login;

import lombok.*;

@Data
public class SocialLoginReqDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login{
        private String provider;
        private String code;
    }
}
