package com.plac.domain.social_login;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo{

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProfileName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getProfileImagePath() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getProfileBirth() {
        return null;
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("email");
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
