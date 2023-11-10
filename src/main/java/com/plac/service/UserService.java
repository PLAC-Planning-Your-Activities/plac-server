package com.plac.service;

import com.plac.domain.RefreshToken;
import com.plac.domain.User;
import com.plac.dto.request.user.UserReqDto;
import com.plac.dto.response.user.UserResDto;
import com.plac.exception.user.DuplUsernameException;
import com.plac.exception.user.UserNotFoundException;
import com.plac.exception.user.UserPrincipalNotFoundException;
import com.plac.repository.RefreshTokenRepository;
import com.plac.repository.UserRepository;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final WeakPasswordChecker passwordChecker;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResDto register(UserReqDto.CreateUser reqDto) throws Exception{
        System.out.println(reqDto);
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

    public User findOne(Long userId) throws Exception {
        Optional<User> findUser = userRepository.findById(userId);
        if(!findUser.isPresent()) {
            throw new UserNotFoundException("해당 [user_id]를 갖는 유저를 찾을 수 없습니다.");
        }
        return findUser.get();
    }

    public void deleteUser() {
        String username = SecurityContextHolderUtil.getUsername();

        Optional<User> findUser = userRepository.findByUsername(username);
        if(!findUser.isPresent()){
            throw new UserPrincipalNotFoundException("유저를 찾을 수 없습니다.");
        }
        User user = findUser.get();
        userRepository.delete(user);

        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(user.getId());

        if(findRefreshToken.isPresent()){
            refreshTokenRepository.delete(findRefreshToken.get());
        }

    }

    public List<UserResDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResDto> userResDtos = new ArrayList<>();
        users.forEach(user->userResDtos.add(UserResDto.of(user)));

        return userResDtos;
    }
}
