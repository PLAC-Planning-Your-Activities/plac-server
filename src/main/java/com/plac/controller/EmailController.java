package com.plac.controller;

import com.plac.dto.request.email.EmailReqDto;
import com.plac.dto.request.email.EmailVerifyReqDto;
import com.plac.service.email.EmailVerificationService;
import com.plac.service.user.UserService;
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
    public ResponseEntity<Void> sendVerificationEmail(
            @Valid @RequestBody EmailReqDto dto
    ) {
        String inputEmail = dto.getEmail();

        userService.checkEmailAvailability(inputEmail);

        emailVerificationService.sendSignupVerificationEmail(inputEmail);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyEmailVerificationCode(
            @Valid @RequestBody EmailVerifyReqDto dto
    ) {
        emailVerificationService.verifySignupEmail(dto.getEmail(), dto.getCode());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
