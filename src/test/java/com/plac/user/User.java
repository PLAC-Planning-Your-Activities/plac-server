package com.plac.user;

import lombok.Getter;

@Getter
public class User {

    String username;

    String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

}
