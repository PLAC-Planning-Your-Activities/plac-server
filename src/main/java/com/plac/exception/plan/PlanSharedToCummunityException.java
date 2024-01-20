package com.plac.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlanSharedToCummunityException extends RuntimeException {

    public PlanSharedToCummunityException() {
        super("플랜이 커뮤니티에 공유되어 있습니다.");
    }
}
