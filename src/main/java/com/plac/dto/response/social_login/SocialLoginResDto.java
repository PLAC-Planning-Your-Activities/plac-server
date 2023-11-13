package com.plac.dto.response.social_login;

import com.plac.domain.User;
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
