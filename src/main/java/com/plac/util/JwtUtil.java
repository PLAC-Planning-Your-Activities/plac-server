package com.plac.util;

import com.plac.domain.user.entity.User;
import com.plac.exception.common.UnAuthorizedException;
import com.plac.security.auth.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Component
@NoArgsConstructor
public class JwtUtil {
    private static long TOKEN_VALIDITY_IN_SECOND = 1000;
    private static long TOKEN_VALIDITY_IN_MINUTE = TOKEN_VALIDITY_IN_SECOND * 60;
    private static long ACCESS_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_IN_MINUTE * 15;   // 15분
    private static long REFRESH_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_IN_MINUTE * 60 * 12;  // 12시간

    public static String createAccessToken(User user, UUID refreshTokenId) {
        return Jwts.builder()
                .setSubject("access_token")
                .setClaims(createAccessTokenClaims(user, refreshTokenId))
                .setExpiration(createTokenExpiration(ACCESS_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getAccessSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createRefreshToken(User user) {
        return Jwts.builder()
                .setSubject("refresh_token")
                .setClaims(createRefreshTokenClaims(user))
                .setExpiration(createTokenExpiration(REFRESH_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getRefreshSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static ResponseCookie makeResponseCookie(String accessToken) {
        ResponseCookie cookies = ResponseCookie.from("plac_token", accessToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(ACCESS_TOKEN_VALIDITY_TIME)     // 15분
                .build();
        return cookies;
    }

    private static Date createTokenExpiration(long expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    private static Key createSigningKey(String tokenSecret) {
        System.out.println(tokenSecret);
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 인가 필터 - access token 만료 시 refresh token의 유효성을 쉽게 조회하기 위해 refresh token id도 함께 넣어준다
    private static Map<String, Object> createAccessTokenClaims(User user, UUID refreshTokenId) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("provider", user.getProvider());
        map.put("refreshTokenId", refreshTokenId);
        return map;
    }

    private static Map<String, Object> createRefreshTokenClaims(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        return map;
    }

    public static String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie cookie;
        try {
            cookie = Arrays.stream(request.getCookies())
                    .filter(r -> r.getName().equals("plac_token"))
                    .findAny()
                    .orElse(null);
        } catch (NullPointerException e) {
            throw new UnAuthorizedException();
        }
        return cookie.getValue();
    }

    public static Claims getClaimFromAccessToken(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(AuthProperties.getAccessSecret())
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
