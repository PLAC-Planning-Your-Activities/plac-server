package com.plac.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class UserUpdateDto {
    @NotBlank(message = "nickname cannot be blank")
    private String nickname;

    @Pattern(regexp = "^(http|https)://.*", message = "Invalid profile image URL")
    private String profileImageUrl;

    @Pattern(regexp = "M|F", message = "Gender must be either 'M' or 'F'")
    private String gender;

    @Min(0)
    @Max(5)
    private int ageGroup;

}
