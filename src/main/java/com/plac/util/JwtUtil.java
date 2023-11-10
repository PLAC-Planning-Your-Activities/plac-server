package com.plac.util;

import com.plac.domain.User;
import com.plac.security.auth.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@NoArgsConstructor
public class JwtUtil {
    private static long TOKEN_VALIDITY_IN_SECOND = 1000;
    private static long ACCESS_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_IN_SECOND * 60 * 15;    // 15분
    private static long REFRESH_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_IN_SECOND * 60 * 60 * 24 * 3;  // 3일

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
        map.put("refreshTokenId", refreshTokenId);
        return map;
    }

    private static Map<String, Object> createRefreshTokenClaims(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        return map;
    }
}
