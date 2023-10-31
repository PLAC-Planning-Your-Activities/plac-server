package com.plac.social_login.domain;

public class GoogleUserInfo implements Oauth2UserInfo {
    @Override
    public String getProvider() {
        return "google";
    }
}
