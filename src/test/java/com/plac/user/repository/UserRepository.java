package com.plac.user.repository;

import com.plac.user.User;

public interface UserRepository {
    void save(User user);

    void delete(User user);

    User findByUsername(String username);

    User findByUserId(Long userId);
}
