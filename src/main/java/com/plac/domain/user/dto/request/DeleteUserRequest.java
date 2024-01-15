package com.plac.domain.user.dto.request;

import lombok.Getter;

@Getter
public class DeleteUserRequest {
    private String password;

    public DeleteUserRequest(String password) {
        this.password = password;
    }
}
