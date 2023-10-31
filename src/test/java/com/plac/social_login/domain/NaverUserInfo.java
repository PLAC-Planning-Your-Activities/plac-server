package com.plac.social_login.domain;

public class NaverUserInfo implements Oauth2UserInfo {
    @Override
    public String getProvider() {
        return "naver";
    }
}
