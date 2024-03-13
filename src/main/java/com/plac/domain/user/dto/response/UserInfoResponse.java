package com.plac.domain.user.dto.response;

import com.plac.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserInfoResponse {
    Long userId;
    String username;
    String profileName;
    String profileImageUrl;
    int age;
    String gender;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .profileName(user.getProfileName())
                .profileImageUrl(user.getProfileImageUrl())
                .age(user.getAge())
                .gender(user.getGender())
                .build();
    }
}