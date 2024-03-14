package com.plac.domain.social_login.entity;

public interface Oauth2UserInfo {

    String getProvider();
    String getUsername();
    String getProfileName();
    String getProfileImagePath();
    String getGender();
    int getAge();
}
