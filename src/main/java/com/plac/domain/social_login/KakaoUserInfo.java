package com.plac.domain.social_login;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo {

    private Map<String ,Object> attributes;
    private Map<String, Object> properties;
    private Map<String, Object> kakaoAccount;
    private Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        properties = (Map)attributes.get("properties");
        kakaoAccount = (Map)attributes.get("kakao_account");
        profile = (Map)kakaoAccount.get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getUsername() {
        return (String)kakaoAccount.get("email");
    }

    @Override
    public String getProfileName() {
        return (String)properties.get("nickname");
    }

    @Override
    public String getProfileImagePath() {
        return (String)properties.get("profile_image");
    }

    @Override
    public String getProfileBirth() {
        return null;
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public int getAge() {
        return 0;
    }
}
