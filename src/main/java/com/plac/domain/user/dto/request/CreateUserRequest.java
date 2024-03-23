package com.plac.domain.user.dto.request;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUserRequest {
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String username;
    private String password;
    private String profileName;
    private int age;
    private String gender;
}