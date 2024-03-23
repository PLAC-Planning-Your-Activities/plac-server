package com.plac.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProfileRequest {
    private String profileName;
    private String profileImageUrl;

    @Pattern(regexp = "^[FM]$", message = "Gender must be 'F' or 'M'")
    private String gender;

    @Min(value = 0, message = "Age group must be between 0 and 5.")
    @Max(value = 5, message = "Age group must be between 0 and 5.")
    private int ageGroup;
}