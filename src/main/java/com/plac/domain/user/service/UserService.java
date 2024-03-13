package com.plac.domain.user.service;

import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.response.CreateUserResponse;
import com.plac.domain.user.dto.request.DeleteUserRequest;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.ConflictException;
import com.plac.exception.common.DataNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordChecker passwordChecker;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;

    public CreateUserResponse signUp(CreateUserRequest userRequest) {
        checkDuplUser(userRequest);
        passwordChecker.checkWeakPassword(userRequest.getPassword());

        User user = createNormalUserInfo(userRequest, userRequest.getPassword());
        userRepository.save(user);

        return new CreateUserResponse(user.getId());
    }

    public void deleteUser() {
        String username = SecurityContextHolderUtil.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new DataNotFoundException("user not found."));

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        userRepository.delete(user);
    }

    public void deleteUser(DeleteUserRequest userRequest) {
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).orElseThrow(
                () -> new DataNotFoundException("uer not found!")
        );

        userRepository.delete(user);
    }


    private void checkDuplUser(CreateUserRequest userRequest) {
        final Optional<User> user = userRepository.findByUsername(userRequest.getUsername());

        if (user.isPresent()) {
            throw new ConflictException("이미 존재하는 이메일입니다. 다른 이메일을 입력하세요.");
        }
    }

    private User createNormalUserInfo(CreateUserRequest userRequest, String password) {
        UUID salt = UUID.randomUUID();
        String encodedPassword = encoder.encode(password + salt);

        int age = userRequest.getAge();
        int ageRange = -1;

        if (age <= 19){
            ageRange = 0;
        }else if (age <= 24) ageRange = 1;
        else if (age <= 29) ageRange = 2;
        else if (age <= 34) ageRange = 3;
        else if (age <= 39) ageRange = 4;
        else if (age >= 40) ageRange = 5;

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(encodedPassword)
                .salt(salt)
                .age(userRequest.getAge())
                .ageRange(ageRange)
                .gender(userRequest.getGender())
                .profileName(userRequest.getProfileName())
                .provider("normal")
                .roles("ROLE_USER")
                .build();
        return user;
    }

    public void checkEmailAvailability(String email) {
        Optional<User> optionalUser = userRepository.findByUsernameAndProvider(email, "normal");

        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }
    }

}
