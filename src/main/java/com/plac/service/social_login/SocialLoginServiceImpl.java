package com.plac.service.social_login;

import com.plac.domain.RefreshToken;
import com.plac.domain.User;
import com.plac.domain.social_login.Oauth2UserInfo;
import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import com.plac.dto.response.social_login.SocialLoginResDto;
import com.plac.exception.social_login.ProviderNotSupportedException;
import com.plac.repository.RefreshTokenRepository;
import com.plac.repository.UserRepository;
import com.plac.service.social_login.oauth2.Oauth2TokenProvider;
import com.plac.service.social_login.oauth2.Oauth2UserInfoProvider;
import com.plac.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginServiceImpl implements SocialLoginService{

    private final String BEARER_TYPE = "Bearer";
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Oauth2TokenProvider oauth2TokenProvider;
    private final Oauth2UserInfoProvider oauth2UserInfoProvider;

    @Override
    public SocialLoginResDto signIn(SocialLoginReqDto.Login req) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(req.getProvider());
        String provider = req.getProvider();

        // Oauth2 Auth 서버에서 인증 토큰을 받아온다.
        Oauth2TokenResDto tokenResponse = getOauth2Token(req, clientRegistration, provider);
        log.info("access_token = {}", tokenResponse.getAccess_token());

        // 받아온 토큰을 바탕으로, 해당 Oauth2 Auth 서버에서 유저 정보를 불러옴
        Map<String, Object> userAttributes = oauth2UserInfoProvider.getUserInfoFromAuthServer(clientRegistration, provider, tokenResponse);
        log.info("user_attributes = {}", userAttributes);

        // 해당 유저 정보 -> Oauth2UserInfo 로 변환
        Oauth2UserInfo oauth2UserInfo = oauth2UserInfoProvider.makeOauth2UserInfo(provider, userAttributes);

        String username = oauth2UserInfo.getUsername();
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        Optional<User> findUser = userRepository.findByUsername(oauth2UserInfo.getUsername());

        User userEntity = findUser.orElseGet(() -> createUser(oauth2UserInfo));
        log.info("userEntity = {}", userEntity);
        if (!findUser.isPresent()){
            userRepository.save(userEntity);
        }

        UUID refreshTokenId = UUID.randomUUID();

        String accessToken = JwtUtil.createAccessToken(userEntity, refreshTokenId);
        String refreshToken = JwtUtil.createRefreshToken(userEntity);

        saveRefreshToken(userEntity, refreshTokenId, refreshToken);

        return SocialLoginResDto.builder()
                .token_type(BEARER_TYPE)
                .user(userEntity)
                .access_token(accessToken)
                .build();
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

    private Oauth2TokenResDto getOauth2Token(SocialLoginReqDto.Login req, ClientRegistration provider, String providerName) {
        if (providerName.equals("naver")){
            return oauth2TokenProvider.getTokenFromNaver(req, provider);
        } else if (providerName.equals("google")){
            return oauth2TokenProvider.getTokenFromGoogle(req, provider);
        }
        throw new ProviderNotSupportedException("해당 소셜로그인은 지원하지 않습니다. 다시 입력하세요.");
    }

    // 사용자 생성 메소드
    private User createUser(Oauth2UserInfo oauth2UserInfo) {
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        return User.builder()
                .username(oauth2UserInfo.getUsername())
                .password(encodedPassword)
                .roles("ROLE_USER")
                .provider(oauth2UserInfo.getProvider())
                .profileImagePath(oauth2UserInfo.getProfileImagePath())
                .profileName(oauth2UserInfo.getProfileName())
                .profileBirth(oauth2UserInfo.getProfileBirth())
                .phoneNumber(oauth2UserInfo.getPhoneNumber())
                .gender(oauth2UserInfo.getGender())
                .createdAt(LocalDateTime.now())
                .age(oauth2UserInfo.getAge())
                .build();
    }
}
