package com.plac.user.repository;

import com.plac.repository.UserRepository;
import com.plac.domain.User;
import com.plac.exception.user.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
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
