package com.plac.exception.common;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(400, "요청 값이 잘못되었습니다. 다시 입력하세요.");
    }

    public BadRequestException(String message) {
        super(400, message);
    }
}