package com.plac.service.social_login;

import com.plac.domain.User;
import com.plac.domain.social_login.Oauth2UserInfo;
import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.Oauth2TokenResDto;
import com.plac.dto.response.social_login.SocialLoginResDto;
import com.plac.repository.UserRepository;
import com.plac.service.social_login.provider.token.TokenProviderContext;
import com.plac.service.social_login.provider.user_info.Oauth2UserInfoContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpySocialLoginServiceImpl implements SocialLoginService{
    private final String BEARER_TYPE = "Bearer";
    private final UserRepository userRepository;
    private final TokenProviderContext tokenProviderContext;
    private final Oauth2UserInfoContext oauth2UserInfoContext;

    @Override
    public SocialLoginResDto signIn(SocialLoginReqDto.Login req) {
        Oauth2TokenResDto tokenResDto = tokenProviderContext.getTokenFromOauth2AuthServer(req);
        Map<String, Object> userInfoFromAuthServer = oauth2UserInfoContext.getUserInfoFromAuthServer(req.getProvider(), tokenResDto);

        Oauth2UserInfo oauth2UserInfo = oauth2UserInfoContext.makeOauth2UserInfo(req.getProvider(), userInfoFromAuthServer);

        Optional<User> findUser = userRepository.findByUsernameAndProvider(oauth2UserInfo.getUsername(), oauth2UserInfo.getProvider());
        User userEntity = findUser.orElseGet(() -> createUser(oauth2UserInfo));

        String accessToken = "plac_access_token";
        return SocialLoginResDto.builder()
                .token_type(BEARER_TYPE)
                .user(userEntity)
                .access_token(accessToken)
                .build();
    }

    private User createUser(Oauth2UserInfo oauth2UserInfo) {
        String password = "password123";

        return User.builder()
                .username(oauth2UserInfo.getUsername())
                .password(password)
                .roles("ROLE_USER")
                .provider(oauth2UserInfo.getProvider())
                .build();
    }
}
