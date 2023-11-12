package com.plac.service.user;

import com.plac.domain.User;
import com.plac.dto.request.user.UserReqDto;
import com.plac.dto.response.user.UserResDto;
import com.plac.exception.user.DuplUsernameException;
import com.plac.exception.user.UserNotFoundException;
import com.plac.exception.user.UserPrincipalNotFoundException;
import com.plac.repository.RefreshTokenRepository;
import com.plac.repository.UserRepository;
import com.plac.service.password_checker.PasswordChecker;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordChecker passwordChecker;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserResDto signUp(UserReqDto.CreateUser reqDto){
        checkDuplUser(reqDto);
        passwordChecker.checkWeakPassword(reqDto.getPassword());

        User user = createUserInfo(reqDto, reqDto.getPassword());
        userRepository.save(user);

        return UserResDto.of(user);
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("헤당 userId를 갖는 유저를 찾을 수 없습니다.")
        );
    }

    @Override
    public void deleteUser() {
        String username = SecurityContextHolderUtil.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserPrincipalNotFoundException("유저를 찾을 수 없습니다.")
        );

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        userRepository.delete(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("헤당 id를 갖는 유저를 찾을 수 없습니다.")
        );

        userRepository.delete(user);
    }

    @Override
    public List<UserResDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResDto::of)
                .collect(Collectors.toList());
    }

    private void checkDuplUser(UserReqDto.CreateUser reqDto) {
        final Optional<User> user = userRepository.findByUsername(reqDto.getUsername());

        if(user.isPresent()){
            throw new DuplUsernameException("이미 존재하는 username(이메일)입니다.");
        }
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


}
