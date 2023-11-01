package com.plac.dto.response;

import com.plac.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class UserResDto {

    private Long id;
    private String username;
    private String profileName;

    public static UserResDto of(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileName(user.getProfileName())
    }
}
