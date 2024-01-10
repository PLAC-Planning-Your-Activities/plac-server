package com.plac.domain.email_verification.controller;

import com.plac.domain.email_verification.dto.request.EmailReqDto;
import com.plac.domain.email_verification.service.EmailVerificationService;
import com.plac.domain.email_verification.dto.request.EmailVerifyReqDto;
import com.plac.domain.user.service.UserService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(
            @Valid @RequestBody EmailReqDto dto
    ) {
        String inputEmail = dto.getEmail();
        System.out.println(inputEmail);

        userService.checkEmailAvailability(inputEmail);

        emailVerificationService.sendSignupVerificationEmail(inputEmail);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyEmailVerificationCode(
            @Valid @RequestBody EmailVerifyReqDto dto
    ) {
        emailVerificationService.verifySignupEmail(dto.getEmail(), dto.getCode());

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }
}
