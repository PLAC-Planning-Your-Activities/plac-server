package com.plac.user;

import com.plac.user.exception.DuplUsernameException;
import com.plac.user.exception.WeakPasswordException;
import com.plac.user.repository.UserRepository;
import com.plac.user.service.EmailNotifier;
import com.plac.user.service.WeakPasswordChecker;

public class UserService {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    private EmailNotifier emailNotifier;
    public UserService(WeakPasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRepository;
        this.emailNotifier = emailNotifier;
    }

    public void register(String username, String password) {
        if (passwordChecker.checkWeakPassword(password)){
            throw new WeakPasswordException();
        }
        User user = userRepository.findByUsername(username);
        if(user != null){
            throw new DuplUsernameException();
        }
        userRepository.save(new User(username, password));
        emailNotifier.sendRegisterEmail(username);
    }
}
