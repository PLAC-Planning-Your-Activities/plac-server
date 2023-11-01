package com.plac.user;

import com.plac.domain.User;
import com.plac.exception.user.DuplUsernameException;
import com.plac.exception.user.WeakPasswordException;
import com.plac.repository.UserRepository;
import com.plac.user.service.EmailNotifier;
import com.plac.service.WeakPasswordChecker;

public class UserService {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    private EmailNotifier emailNotifier;
    private Long num;

    public UserService(WeakPasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRepository;
        this.emailNotifier = emailNotifier;
        this.num = 1L;
    }

    public Long register(String username, String password) {
        if (passwordChecker.checkWeakPassword(password)){
            throw new WeakPasswordException();
        }
        User user = userRepository.findByUsername(username);
        if(user != null){
            throw new DuplUsernameException();
        }
        userRepository.save(new User(num++, username, password));
        emailNotifier.sendRegisterEmail(username);
        return num-1;
    }

    public void delete(User findUser) {
        userRepository.delete(findUser);
    }
}
