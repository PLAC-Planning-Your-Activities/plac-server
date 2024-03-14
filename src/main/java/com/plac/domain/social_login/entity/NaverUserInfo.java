package com.plac.domain.social_login.entity;

import java.util.Map;

public class NaverUserInfo implements Oauth2UserInfo{

    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProfileName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getProfileImagePath() {
        return (String) attributes.get("profile_image");
    }

    @Override
    public String getGender() {
        return (String) attributes.get("gender");
    }

    @Override
    public int getAge() {
        return 2023 - Integer.parseInt((String) attributes.get("birthyear"));
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("email");
    }
}
