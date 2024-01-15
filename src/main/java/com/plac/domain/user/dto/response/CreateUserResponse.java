package com.plac.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateUserResponse {

    private Long userId;

    @Builder
    public CreateUserResponse(Long userId) {
        this.userId = userId;
    }
}
