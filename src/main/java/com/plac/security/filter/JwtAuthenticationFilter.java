package com.plac.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plac.domain.user.dto.response.UserInfoResponse;
import com.plac.domain.user.entity.RefreshToken;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.exception.BaseErrorResponse;
import com.plac.exception.common.BadRequestException;
import com.plac.security.auth.CustomUserDetails;
import com.plac.util.JwtUtil;
import com.plac.util.ResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = new ObjectMapper().readValue(request.getReader(), User.class);
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            this.setDetails(request, userToken);

            return getAuthenticationManager().authenticate(userToken);
        } catch (IOException e) {
            throw new BadRequestException("아이디와 비밀번호를 올바르게 입력해주세요.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = ((CustomUserDetails) authResult.getPrincipal()).getUser();

        UUID refreshTokenId = UUID.randomUUID();
        String accessToken = JwtUtil.createAccessToken(user, refreshTokenId);
        String refreshToken = JwtUtil.createRefreshToken(user);

        saveRefreshToken(user, refreshTokenId, refreshToken);

        ResponseCookie cookies = JwtUtil.makeResponseCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookies.toString());

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .profileName(user.getProfileName())
                .profileImageUrl(user.getProfileImageUrl())
                .age(user.getAge())
                .gender(user.getGender())
                .build();

        ResponseUtil.setResponse(response, userInfoResponse, HttpStatus.OK);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        Object failedType = failed.getClass();

        if(failedType.equals(BadCredentialsException.class) || failedType.equals(UsernameNotFoundException.class)) {
            ResponseUtil.setResponse(response, new BaseErrorResponse("로그인 정보가 올바르지 않습니다."), HttpStatus.UNAUTHORIZED);
        } else {
            ResponseUtil.setResponse(response, new BaseErrorResponse(failed.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    private void saveRefreshToken(User user, UUID refreshTokenId, String refreshToken) {
        try{
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(user.getId());

            if(refreshTokenOpt.isPresent())
                refreshTokenRepository.delete(refreshTokenOpt.get());

            RefreshToken newRefreshToken = buildRefreshToken(user, refreshTokenId, refreshToken);
            refreshTokenRepository.save(newRefreshToken);
        }catch(NullPointerException e) {
            throw new AuthenticationServiceException("유효하지 않은 사용자입니다.");
        }
    }

    private static RefreshToken buildRefreshToken(User user, UUID refreshTokenId, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();
        return newRefreshToken;
    }
}