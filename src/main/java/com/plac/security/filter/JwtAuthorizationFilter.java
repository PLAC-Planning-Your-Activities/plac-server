package com.plac.security.filter;

import com.plac.domain.user.entity.RefreshToken;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.RefreshTokenRepository;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.DataNotFoundException;
import com.plac.security.auth.AuthProperties;
import com.plac.security.auth.CustomUserDetails;
import com.plac.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 1. 액세스토큰을 담고 있는 쿠키를 확인
    // 2. 액세스토큰이 유효하다면 -> 인증된 객체 저장하고 doFilter, 그렇지 않다면 -> 리프레스토큰 검사
    // 3. DB에서 리프레시토큰 조회. 리프레시 토큰이 유효하다면 -> 새로운 액세스토큰 발급, 그렇지 않다면 -> 인증된 객체를 저장하지 않고 doFilter

    private final List<RequestMatcher> excludedUrlPatterns = Arrays.asList(//이 필터 적용 안할 url 지정
            new AntPathRequestMatcher("/login/mailConfirm"),
            new AntPathRequestMatcher("/login/authenticate")
            // 추가적인 URL 패턴을 필요에 따라 설정
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isExcludedUrl(request)) {
            filterChain.doFilter(request, response);//이 필터 스킵. 다음꺼 실행.
            return;
        }
        
        Cookie cookie = checkAccessToken(request, response, filterChain);
        if(cookie == null) {
            //filterChain.doFilter(request, response);
            return;
        }
        
        String accessToken = cookie.getValue();
        
        boolean isAccessTokenExpired = false;
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder().setSigningKey(AuthProperties.getAccessSecret()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
            isAccessTokenExpired = true;
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에 저장된 유저정보가 존재하지 않는 경우 예외처리
        Optional<User> savedUser = userRepository.findByUsernameAndProvider(claims.get("username").toString(), claims.get("provider").toString());
        savedUser.orElseThrow(() -> new DataNotFoundException("엑세스 토큰에 저장된 유저 정보가 존재하지 않습니다."));

        // 액세스토큰이 만료된 경우
        if(isAccessTokenExpired) {
            if (issueNewAccessToken(request, response, filterChain, claims, savedUser.get())) return;
        }

        this.saveAuthenticationToSecurityContextHolder(savedUser.get());
        filterChain.doFilter(request, response);
    }

    private boolean issueNewAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Claims claims, User savedUser) throws IOException, ServletException {
        UUID refreshTokenId = UUID.fromString(claims.get("refreshTokenId").toString());
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findById(refreshTokenId);

        // 액세스토큰과 매칭된 리프레시토큰을 DB에서 조회한다
        if(refreshTokenOpt.isPresent()) {
            Claims refreshTokenClaims = null;
            String refreshToken = refreshTokenOpt.get().getRefreshToken();

            try {
                refreshTokenClaims = Jwts.parserBuilder().setSigningKey(AuthProperties.getRefreshSecret()).build().parseClaimsJws(refreshToken).getBody();
            } catch (ExpiredJwtException e) {
                // 리프레시 토큰이 만료된 경우
                // 만료된 리프레시 토큰을 제거 후 doFitler
                refreshTokenRepository.delete(refreshTokenOpt.get());
                filterChain.doFilter(request, response);
                return true;
            } catch (MalformedJwtException e) {
                filterChain.doFilter(request, response);
                return true;
            } catch (Exception e) {     // TODO :: 구체 예외 처리
                filterChain.doFilter(request, response);
                return true;
            }

            // 리프레시 토큰이 존재한다면 액세스토큰 발급
            String newAccessToken = JwtUtil.createAccessToken(savedUser, refreshTokenId);

            if(refreshTokenClaims != null) {
                ResponseCookie resCookie = JwtUtil.makeResponseCookie(newAccessToken);
                response.addHeader(HttpHeaders.SET_COOKIE, resCookie.toString());
            }
        }else {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private static Cookie checkAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Cookie cookie;
        try {
            cookie = Arrays.stream(request.getCookies())
                    .filter(r -> r.getName().equals("plac_token"))
                    .findAny()
                    .orElse(null);
        } catch (NullPointerException e) {
            filterChain.doFilter(request, response);
            return null;
        }
        return cookie;
    }

    private void saveAuthenticationToSecurityContextHolder(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 인가 처리가 정상적으로 완료된다면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isExcludedUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return excludedUrlPatterns.stream().anyMatch(pattern -> pattern.matches(request));
    }
}