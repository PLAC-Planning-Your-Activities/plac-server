package com.plac.domain.user.dto.response;

import lombok.Builder;

public class CreateUserResponse {

    private Long userId;

    @Builder
    public CreateUserResponse(Long userId) {
        this.userId = userId;
    }
}
