package com.plac.exception.custom;

import com.plac.exception.common.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WeakPasswordException extends BaseException {

    public WeakPasswordException() {
        super(400, "비밀번호가 너무 약합니다. 다시 설정해주세요.");
    }
}
