package com.plac.social_login.service;

import com.plac.exception.user.WeakPasswordException;
import com.plac.user.service.EmailNotifier;
import com.plac.service.password_checker.PasswordChecker;
import com.plac.repository.UserRepository;

public class Oauth2UserService {

    PasswordChecker passwordChecker;
    UserRepository userRepository;
    EmailNotifier emailNotifier;

    public Oauth2UserService(PasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRepository;
        this.emailNotifier = emailNotifier;
    }

    public void register(String s, String pw) {
        if(passwordChecker.checkWeakPassword(pw)){
            throw new WeakPasswordException("비밀번호가 약합니다.");
        }
    }
}
