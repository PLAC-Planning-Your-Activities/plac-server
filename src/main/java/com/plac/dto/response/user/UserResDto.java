package com.plac.dto.response.user;

import com.plac.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserResDto {

    private Long id;
    private String username;
    private String profile_name;
    private String profile_image_path;
    private String profile_birth;
    private int age;
    private String gender;
    private String phoneNumber;
    private LocalDateTime created_at;
    private String provider;

    public static UserResDto of(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profile_name(user.getProfileName())
                .profile_image_path(user.getProfileImagePath())
                .profile_birth(user.getProfileBirth())
                .age(user.getAge())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .created_at(user.getCreatedAt())
                .provider(user.getProvider())
                .build();
    }
}
