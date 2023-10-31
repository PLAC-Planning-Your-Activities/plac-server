package com.plac.user.repository;

import com.plac.user.User;
import com.plac.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getUserId());
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findByUserId(Long userId) {
        User user = users.get(userId);
        if(user == null)
            throw new UserNotFoundException();
        return user;
    }
}
