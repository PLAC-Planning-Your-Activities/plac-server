package com.plac.domain.user.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUserRequest {
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String username;
    private String password;
    private String profileName;

    @Min(value = 0, message = "ageGroup은 0 이상이어야 합니다.")
    @Max(value = 5, message = "ageGroup은 5 이하여야 합니다.")
    private int ageGroup;
    private String gender;
}