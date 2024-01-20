package com.plac.domain.user.dto.response;

import com.plac.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LoginUserInfo {
    private Long userId;
    private String username;
    private String profileName;
    private String profileImageUrl;
    private int age;
    private int ageRange;
    private String gender;
    private String phoneNumber;

    public static LoginUserInfo of(User user) {
        return LoginUserInfo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .profileName(user.getProfileName())
                .profileImageUrl(user.getProfileImageUrl())
                .age(user.getAge())
                .ageRange(user.getAgeRange())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
