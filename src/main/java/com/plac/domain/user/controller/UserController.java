package com.plac.domain.user.controller;

import com.plac.domain.email_verification.dto.request.EmailReqDto;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.request.DeleteUserRequest;
import com.plac.domain.user.service.UserService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<?> signUp(@RequestBody CreateUserRequest userRequest) throws Exception {
        userService.signUp(userRequest);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest userRequest) throws Exception {
        userService.deleteUser(userRequest);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @GetMapping("/emails/availability")
    public ResponseEntity<Void> checkEmailAvailability(
            @Valid @RequestBody EmailReqDto dto
    ) {
        String inputEmail = dto.getEmail();

        userService.checkEmailAvailability(inputEmail);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
