package com.plac.domain.user.repository;

import com.plac.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findById(Long id);

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndProvider(String username, String provider);

    Optional<User> findByProfileName(String profileName);
}
