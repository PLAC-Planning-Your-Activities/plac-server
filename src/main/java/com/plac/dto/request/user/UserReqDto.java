package com.plac.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {

    private String username;
    private String password;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateUser{
        private String username;
        private String password;
        private String profileName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteUser{
        private String username;
        private String password;
    }
}
