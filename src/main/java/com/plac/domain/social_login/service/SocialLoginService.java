package com.plac.domain.social_login.service;

import com.plac.domain.user.entity.RefreshToken;
import com.plac.domain.user.entity.User;
import com.plac.domain.social_login.entity.Oauth2UserInfo;
import com.plac.domain.social_login.dto.SocialLoginReqDto;
import com.plac.domain.social_login.dto.Oauth2TokenResDto;
import com.plac.domain.social_login.dto.SocialLoginResDto;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.domain.user.repository.UserRepository;
import com.plac.domain.social_login.provider.token.TokenProviderContext;
import com.plac.domain.social_login.provider.user_info.Oauth2UserInfoContext;
import com.plac.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    private final String BEARER_TYPE = "Bearer";
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProviderContext tokenProviderContext;
    private final Oauth2UserInfoContext oauth2UserInfoContext;

    public SocialLoginResDto signIn(SocialLoginReqDto.Login req) {
        Oauth2TokenResDto tokenResDto = tokenProviderContext.getTokenFromOauth2AuthServer(req);
        Map<String, Object> userInfoFromAuthServer = oauth2UserInfoContext.getUserInfoFromAuthServer(req.getProvider(), tokenResDto);

        Oauth2UserInfo oauth2UserInfo = oauth2UserInfoContext.makeOauth2UserInfo(req.getProvider(), userInfoFromAuthServer);

        Optional<User> findUser = userRepository.findByUsernameAndProvider(oauth2UserInfo.getUsername(), oauth2UserInfo.getProvider());
        User userEntity = findUser.orElseGet(() -> createUser(oauth2UserInfo));
        log.info("userEntity = {}", userEntity);

        if (!findUser.isPresent()){
            userRepository.save(userEntity);
        }

        String accessToken = processAccessAndRefreshToken(userEntity);

        return SocialLoginResDto.builder()
                .token_type(BEARER_TYPE)
                .user(userEntity)
                .access_token(accessToken)
                .build();
    }

    private String processAccessAndRefreshToken(User userEntity) {
        UUID refreshTokenId = UUID.randomUUID();

        String accessToken = JwtUtil.createAccessToken(userEntity, refreshTokenId);
        String refreshToken = JwtUtil.createRefreshToken(userEntity);

        saveRefreshToken(userEntity, refreshTokenId, refreshToken);
        return accessToken;
    }

    private void saveRefreshToken(User userEntity, UUID refreshTokenId, String refreshToken) {
        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUserId(userEntity.getId());

        if(oldRefreshToken.isPresent()){ // 해당 사용자의 리프레쉬 토큰이 이미 있다면 삭제한다.
            refreshTokenRepository.delete(oldRefreshToken.get());
        }
        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .refreshToken(refreshToken)
                .userId(userEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(newRefreshToken);
    }

    private User createUser(Oauth2UserInfo oauth2UserInfo) {
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        return User.builder()
                .username(oauth2UserInfo.getUsername())
                .password(encodedPassword)
                .roles("ROLE_USER")
                .provider(oauth2UserInfo.getProvider())
                .profileImageUrl(oauth2UserInfo.getProfileImagePath())
                .profileName(oauth2UserInfo.getProfileName())
                .profileBirthday(oauth2UserInfo.getProfileBirth())
                .phoneNumber(oauth2UserInfo.getPhoneNumber())
                .gender(oauth2UserInfo.getGender())
                .age(oauth2UserInfo.getAge())
                .build();
    }
}
