package com.plac.domain.social_login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plac.domain.social_login.dto.SocialLoginRequest;
import com.plac.domain.social_login.dto.SocialLoginResponse;
import com.plac.domain.social_login.service.SocialLoginService;
import com.plac.domain.user.dto.response.UserInfoResponse;
import com.plac.domain.user.entity.User;
import com.plac.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/social-login")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping
    public void signIn(HttpServletResponse response, @RequestBody SocialLoginRequest socialLoginRequest) throws Exception {
        SocialLoginResponse socialLoginResponse = socialLoginService.signIn(socialLoginRequest);
        User userEntity = socialLoginResponse.getUser();

        ObjectMapper om = setResponseCookie(response, socialLoginResponse);

        if (userEntity.getAgeGroup() == -1 && userEntity.getGender().equals("M")) {
            response.setStatus(HttpStatus.ACCEPTED.value());
        } else response.setStatus(HttpStatus.OK.value());

        UserInfoResponse userInfo = UserInfoResponse.of(userEntity);
        om.writeValue(response.getOutputStream(), userInfo);
    }

    private ObjectMapper setResponseCookie(HttpServletResponse response, SocialLoginResponse socialLoginResponse) {
        ResponseCookie responseCookie = JwtUtil.makeResponseCookie(socialLoginResponse.getAccess_token());
        ObjectMapper om = settingObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return om;
    }

    private ObjectMapper settingObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }
}
