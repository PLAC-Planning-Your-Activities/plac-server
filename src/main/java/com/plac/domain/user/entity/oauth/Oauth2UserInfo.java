package com.plac.domain.user.entity.oauth;

public interface Oauth2UserInfo {

    String getProvider();
    String getUsername();
    String getProfileName();
    String getProfileImagePath();
    String getProfileBirth();
    String getPhoneNumber();
    String getGender();
    int getAge();
}
