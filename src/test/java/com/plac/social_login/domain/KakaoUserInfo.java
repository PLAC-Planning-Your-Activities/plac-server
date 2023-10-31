package com.plac.social_login.domain;

public class KakaoUserInfo implements Oauth2UserInfo {
    @Override
    public String getProvider() {
        return "kakao";
    }
}
