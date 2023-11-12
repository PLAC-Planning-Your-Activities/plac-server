package com.plac.security.auth;

import com.plac.domain.User;
import com.plac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService - loadUserByUsername() ====");
        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            return new CustomUserDetails(userOpt.get());
        }else {
            throw new UsernameNotFoundException("로그인 정보가 올바르지 않습니다.");
        }
    }

}