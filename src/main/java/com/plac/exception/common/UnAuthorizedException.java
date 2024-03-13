package com.plac.exception.common;

public class UnAuthorizedException extends BaseException {

    public UnAuthorizedException() {
        super(401, "authorization failed");
    }
}
