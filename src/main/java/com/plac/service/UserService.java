package com.plac.service;

import com.plac.domain.User;
import com.plac.dto.request.UserReqDto;
import com.plac.dto.response.UserResDto;
import com.plac.exception.user.DuplUsernameException;
import com.plac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final WeakPasswordChecker passwordChecker;

    public UserResDto register(UserReqDto.CreateUser reqDto) throws Exception{

        checkDuplUser(reqDto);
        passwordChecker.checkWeakPassword(reqDto.getPassword());

        User user = createUserInfo(reqDto, reqDto.getPassword());
        userRepository.save(user);

        return UserResDto.of(user);
    }

    private void checkDuplUser(UserReqDto.CreateUser reqDto) {
        if(userRepository.findByUsername(reqDto.getUsername()).isPresent())
            throw new DuplUsernameException("이미 존재하는 이메일입니다.");
    }

    private User createUserInfo(UserReqDto.CreateUser reqDto, String password) {
        UUID salt = UUID.randomUUID();
        String encodedPassword = encoder.encode(password + salt.toString());

        User user = User.builder()
                .username(reqDto.getUsername())
                .password(encodedPassword)
                .salt(salt)
                .roles("ROLE_USER")
                .profileName(reqDto.getProfileName())
                .createdAt(LocalDateTime.now())
                .build();
        return user;
    }

    public void deleteUser() {

    }
}
