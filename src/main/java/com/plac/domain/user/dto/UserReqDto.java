package com.plac.domain.user.dto;

import lombok.*;


public class UserReqDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateUser{
        private String username;
        private String password;
        private String profileName;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteUser{
        private String username;
        private String password;
    }
}
