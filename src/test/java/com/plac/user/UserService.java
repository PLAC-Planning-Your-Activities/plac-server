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
