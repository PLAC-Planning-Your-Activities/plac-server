package com.plac.user.repository;

import com.plac.user.User;

public interface UserRepository {
    void save(User pw123);

    User findByUsername(String username);
}
