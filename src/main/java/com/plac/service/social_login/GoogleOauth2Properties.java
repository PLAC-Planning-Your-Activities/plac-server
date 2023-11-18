package com.plac.service.social_login;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.google")
@Getter
@Setter
public class GoogleOauth2Properties{
    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
    private String userInfoEndpoint;
}
