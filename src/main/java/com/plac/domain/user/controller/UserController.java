package com.plac.domain.user.controller;

import com.plac.domain.email_verification.dto.request.EmailReqDto;
import com.plac.domain.user.dto.UserUpdateDto;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.request.DeleteUserRequest;
import com.plac.domain.user.dto.response.LoginUserInfo;
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
    public ResponseEntity<?> signUp(
            @RequestBody @Valid CreateUserRequest userRequest
    ) {
        userService.signUp(userRequest);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest userRequest) {
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

    @PutMapping("{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserUpdateDto updateDto
    ) {
        LoginUserInfo result = userService.updateUser(userId, updateDto);
        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }
}
