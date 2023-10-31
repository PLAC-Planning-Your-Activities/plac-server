package com.plac.user.service;

public class SpyEmailNotifier implements EmailNotifier{

    private boolean called;
    private String email;

    @Override
    public void sendRegisterEmail(String email) {
        this.called = true;
        this.email = email;
    }

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }
}
