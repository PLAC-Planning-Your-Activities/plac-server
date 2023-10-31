package com.plac.user;

import com.plac.user.exception.WeakPasswordException;

public class UserService {
    private WeakPasswordChecker passwordChecker;
    public UserService(WeakPasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;
    }

    public void register(String username, String password) {
        if (passwordChecker.checkWeakPassword(password)){
            throw new WeakPasswordException();
        }
    }
}
