package com.plac.dto.request.email;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailVerifyReqDto {

    @NotBlank(message = "이메일은 공백일 수 없습니다")
    @Email(message = "유효한 이메일 형식이 아닙니다")
    private String email;

    @NotNull
    private int code;
}
