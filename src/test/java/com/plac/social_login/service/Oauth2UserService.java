package com.plac.social_login.service;

import com.plac.exception.user.WeakPasswordException;
import com.plac.user.service.EmailNotifier;
import com.plac.service.WeakPasswordChecker;
import com.plac.repository.UserRepository;

public class Oauth2UserService {

    WeakPasswordChecker passwordChecker;
    UserRepository userRepository;
    EmailNotifier emailNotifier;

    public Oauth2UserService(WeakPasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
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
