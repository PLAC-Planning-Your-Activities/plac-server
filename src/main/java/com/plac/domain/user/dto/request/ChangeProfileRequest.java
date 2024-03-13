package com.plac.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeProfileRequest {
    private String profileName;
    private String profileImageUrl;
    private String gender;
    private int ageGroup;

    @Builder
    public ChangeProfileRequest(String profileName, String profileImageUrl, String gender, int ageGroup) {
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }
}