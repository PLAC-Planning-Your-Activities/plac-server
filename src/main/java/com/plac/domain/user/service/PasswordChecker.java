package com.plac.domain.user.service;

import com.plac.exception.custom.WeakPasswordException;
import org.springframework.stereotype.Service;

@Service
public class PasswordChecker {

    public boolean checkWeakPassword(String password) {
        if(password.length() < 5 || password.length() > 16)
            throw new WeakPasswordException();
        return false;
    }

}
