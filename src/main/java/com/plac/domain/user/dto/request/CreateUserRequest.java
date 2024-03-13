package com.plac.domain.user.dto.request;

import lombok.*;

@Getter
public class CreateUserRequest {
    private String username;
    private String password;
    private String profileName;
    private int age;
    private String gender;

    @Builder
    public CreateUserRequest(String username, String password, String profileName, int age, String gender) {
        this.username = username;
        this.password = password;
        this.profileName = profileName;
        this.age = age;
        this.gender = gender;
    }
}