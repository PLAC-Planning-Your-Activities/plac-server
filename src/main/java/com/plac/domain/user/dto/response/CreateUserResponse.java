package com.plac.domain.user.dto.response;

import lombok.Getter;

@Getter
public class CreateUserResponse {

    private Long userId;

    public CreateUserResponse(Long userId) {
        this.userId = userId;
    }
}