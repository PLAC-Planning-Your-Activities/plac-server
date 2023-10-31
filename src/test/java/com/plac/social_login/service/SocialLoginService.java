package com.plac.social_login.service;

import com.plac.social_login.exception.WeakPasswordException;
import com.plac.social_login.repository.EmailNotifier;
import com.plac.social_login.repository.WeakPasswordChecker;
import com.plac.social_login.repository.UserRepository;

public class SocialLoginService {

    WeakPasswordChecker passwordChecker;
    UserRepository userRepository;
    EmailNotifier emailNotifier;

    public SocialLoginService(WeakPasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRepository;
        this.emailNotifier = emailNotifier;
    }

    public void register(String s, String pw) {
        if(passwordChecker.checkWeakPassword(pw)){
            throw new WeakPasswordException();
        }
    }
}
