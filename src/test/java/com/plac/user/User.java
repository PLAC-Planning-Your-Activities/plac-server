package com.plac.user;

import lombok.Getter;

import javax.persistence.Id;

@Getter
public class User {

    @Id
    Long userId;
    String username;

    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public Long getUserId() {
        return userId;
    }
}
