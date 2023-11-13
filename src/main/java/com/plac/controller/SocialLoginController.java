package com.plac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plac.domain.Message;
import com.plac.dto.request.social_login.SocialLoginReqDto;
import com.plac.dto.response.social_login.SocialLoginResDto;
import com.plac.service.social_login.SocialLoginService;
import com.plac.util.JwtUtil;
import com.plac.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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

    @Operation(summary = "소셜 로그인 api", description = "소셜 로그인 수행")
    @PostMapping("")
    public void signIn(HttpServletResponse response, @RequestBody SocialLoginReqDto.Login req) throws Exception {
        ObjectMapper om = settingObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        
        SocialLoginResDto loginResponse = socialLoginService.signIn(req);
        Message message = MessageUtil.buildMessage(HttpStatus.OK, "소셜로그인 성공. 엑세스 토큰을 발급합니다.");

        ResponseCookie responseCookie = JwtUtil.makeResponseCookie(loginResponse.getAccess_token());

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        om.writeValue(response.getOutputStream(), message);
    }

    private static ObjectMapper settingObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }
}
