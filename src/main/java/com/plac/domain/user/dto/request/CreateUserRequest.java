package com.plac.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserRequest {
    private String username;
    private String password;
    private String profileName;
    @NotNull(message = "나이를 입력해주세요")
    @Min(value = 1, message = "나이는 1 이상이어야 합니다")
    private int age;
    @NotBlank(message = "성별을 입력해주세요")
    @Pattern(regexp = "^[FM]$", message = "성별은 'F' 또는 'M'이어야 합니다")
    private String gender;

    @Builder
    public CreateUserRequest(String username, String password, String profileName, int age, String gender) {
        this.username = username;
        this.password = password;
        this.profileName = profileName;
        this.age = age;
        this.gender = gender;
    }
}
