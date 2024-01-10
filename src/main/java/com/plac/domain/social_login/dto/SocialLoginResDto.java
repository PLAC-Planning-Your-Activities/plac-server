package com.plac.domain.social_login.dto;

import com.plac.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResDto {
    private String token_type;
    private String access_token;
    private User user;
}
