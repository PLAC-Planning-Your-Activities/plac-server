package com.plac.security.filter;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plac.common.Message;
import com.plac.domain.user.entity.RefreshToken;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.dto.UserResDto;
import com.plac.exception.user.WrongLoginException;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.security.auth.CustomUserDetails;
import com.plac.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        // form 로그인이 아닌, 커스텀 로그인에서 api 요청시 인증 필터를 진행할 url
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws WrongLoginException {
        System.out.println("=== JwtAuthenticationFilter - attemptAuthentication() ====");
        try {
            // form으로 넘어온 값으로 user 객체를 생성
            User user = new ObjectMapper().readValue(request.getReader(), User.class);
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            this.setDetails(request, userToken);

            // AuthenticationManager에 인증 위임
            return getAuthenticationManager().authenticate(userToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("아이디와 비밀번호를 올바르게 입력해주세요.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("=== JwtAuthenticationFilter - successfulAuthentication() ====");

        // 1. 로그인 성공된 user 조회
        User user = ((CustomUserDetails) authResult.getPrincipal()).getUser();

        // 2. Access Token, Refresh Token 생성
        UUID refreshTokenId = UUID.randomUUID();
        String accessToken = JwtUtil.createAccessToken(user, refreshTokenId);
        String refreshToken = JwtUtil.createRefreshToken(user);

        // 3. Refresh Token DB 저장 (해당 유저의 리프레시 토큰이 이미 존재한다면, 삭제 후 저장)
        saveRefreshToken(user, refreshTokenId, refreshToken);

        // 4. Cookie에 Access Token (access_token) 주입
        ResponseCookie cookies = JwtUtil.makeResponseCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookies.toString());

        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setData(UserResDto.of(user));
        message.setMessage("login_success");

        this.createResponseMessage(response, message, HttpStatus.OK);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        System.out.println("=== JwtAuthenticationFilter - unsuccessfulAuthentication() ====");
        // 1. Http Response Message 세팅 후 반환
        Object failedType = failed.getClass();

        // 2. 예외에 따른 response 세팅
        if(failedType.equals(BadCredentialsException.class) || failedType.equals(UsernameNotFoundException.class)) {
            Message message = new Message();
            message.setStatus(HttpStatus.UNAUTHORIZED);
            message.setMessage(failed.getLocalizedMessage());

            this.createResponseMessage(response, message, HttpStatus.UNAUTHORIZED);
        } else {
            Message message = new Message();
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage(failed.getLocalizedMessage());

            this.createResponseMessage(response, message, HttpStatus.UNAUTHORIZED);
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

    // response message 설정
    private void createResponseMessage(HttpServletResponse response, Message message, HttpStatus status) throws StreamWriteException, DatabindException, IOException {
        response.setStatus(status.value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        objectMapper.writeValue(response.getOutputStream(), message);
    }
}