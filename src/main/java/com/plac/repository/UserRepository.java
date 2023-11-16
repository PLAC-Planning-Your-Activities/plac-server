package com.plac.repository;

import com.plac.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional <User> findById(Long id);

    public Optional<User> findByUsername(String username);
    public Optional<User> findByUsernameAndProvider(String username, String provider);

}
