package com.plac.domain.user.service;

import com.plac.domain.user.dto.request.ChangeProfileRequest;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.response.CreateUserResponse;
import com.plac.domain.user.dto.response.UserInfoResponse;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.ConflictException;
import com.plac.exception.common.DataNotFoundException;
import com.plac.exception.common.UnAuthorizedException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${plac.s3.defaultImageUrl}")
    private String defaultImageUrl;

    public CreateUserResponse signUp(CreateUserRequest userRequest) {
        checkDuplUser(userRequest);
        passwordChecker.checkWeakPassword(userRequest.getPassword());

        User user = createNormalUserInfo(userRequest, userRequest.getPassword());
        userRepository.save(user);

        return new CreateUserResponse(user.getId());
    }

    public void deleteUser() {
        Long userId = SecurityContextHolderUtil.getUserId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("user not found."));

        refreshTokenRepository.findByUserId(userId)
                .ifPresent(refreshTokenRepository::delete);

        userRepository.delete(user);
    }

    private void checkDuplUser(CreateUserRequest userRequest) {
        final Optional<User> user = userRepository.findByUsername(userRequest.getUsername());

        if (user.isPresent()) {
            throw new ConflictException("이미 존재하는 이메일입니다. 다른 이메일을 입력하세요.");
        }
    }

    private User createNormalUserInfo(CreateUserRequest userRequest, String password) {
        int ageRange = getAgeRange(userRequest);

        String encodedPassword = encoder.encode(password);

        return User.builder()
                .username(userRequest.getUsername())
                .password(encodedPassword)
                .age(userRequest.getAge())
                .ageRange(ageRange)
                .gender(userRequest.getGender())
                .profileName(userRequest.getProfileName())
                .profileImageUrl(defaultImageUrl)
                .provider("normal")
                .roles("ROLE_USER")
                .build();
    }

    private static int getAgeRange(CreateUserRequest userRequest) {
        int age = userRequest.getAge();
        int ageRange = -1;

        if (age <= 19){
            ageRange = 0;
        }else if (age <= 24) ageRange = 1;
        else if (age <= 29) ageRange = 2;
        else if (age <= 34) ageRange = 3;
        else if (age <= 39) ageRange = 4;
        else if (age >= 40) ageRange = 5;
        return ageRange;
    }

    public void checkEmailAvailability(String email) {
        Optional<User> optionalUser = userRepository.findByUsernameAndProvider(email, "normal");

        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }
    }

    @Transactional
    public UserInfoResponse changeProfile(Long userId, ChangeProfileRequest changeProfileRequest) {
        if (userId != SecurityContextHolderUtil.getUserId()) {
            throw new UnAuthorizedException();
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("user not found")
        );

        user.changeProfile(changeProfileRequest);
        return UserInfoResponse.of(user);
    }
}
