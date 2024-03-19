package com.plac.domain.user.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangeProfileRequest {
    private String profileName;
    private String profileImageUrl;
    private String gender;
    private int ageGroup;
}