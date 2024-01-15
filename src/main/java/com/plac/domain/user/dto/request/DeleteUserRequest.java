package com.plac.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteUserRequest {
    private String password;

    public DeleteUserRequest(String password) {
        this.password = password;
    }
}
