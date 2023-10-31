package com.plac.user;

import com.plac.user.exception.DuplUsernameException;
import com.plac.user.exception.WeakPasswordException;
import com.plac.user.repository.UserRepository;
import com.plac.user.service.WeakPasswordChecker;

public class UserService {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    public UserService(WeakPasswordChecker passwordChecker, UserRepository userRepository) {
        this.passwordChecker = passwordChecker;
        this.userRepository = userRepository;
    }

    public void register(String username, String password) {
        if (passwordChecker.checkWeakPassword(password)){
            throw new WeakPasswordException();
        }
        User user = userRepository.findByUsername(username);
        if(user != null){
            throw new DuplUsernameException();
        }
    }
}
