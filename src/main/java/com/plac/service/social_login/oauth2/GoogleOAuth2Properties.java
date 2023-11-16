package com.plac.service.social_login.oauth2;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.google")
@Getter
@Setter
public class GoogleOAuth2Properties {
    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;

    // getters and setters
}
