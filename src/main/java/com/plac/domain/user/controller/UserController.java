package com.plac.domain.user.controller;

import com.plac.domain.email_verification.dto.request.EmailReqDto;
import com.plac.domain.user.dto.request.ChangeProfileRequest;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.response.UserInfoResponse;
import com.plac.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody CreateUserRequest userRequest) {
        userService.signUp(userRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<UserInfoResponse> changeProfile(@RequestBody ChangeProfileRequest changeProfileRequest) {
        UserInfoResponse userInfo = userService.changeProfile(changeProfileRequest);
        return ResponseEntity.ok().body(userInfo);
    }

    @GetMapping("/emails/availability")
    public ResponseEntity<Void> checkEmailAvailability(@Valid @RequestBody EmailReqDto dto) {
        userService.checkEmailAvailability(dto.getEmail());
        return ResponseEntity.ok().build();
    }
}