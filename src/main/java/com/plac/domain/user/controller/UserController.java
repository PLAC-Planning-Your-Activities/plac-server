package com.plac.domain.user.controller;

import com.plac.domain.email_verification.dto.request.EmailReqDto;
import com.plac.domain.user.dto.request.CreateUserRequest;
import com.plac.domain.user.dto.request.DeleteUserRequest;
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
    public ResponseEntity<Void> signUp(@RequestBody CreateUserRequest userRequest) throws Exception {
        userService.signUp(userRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserRequest userRequest) throws Exception {
        userService.deleteUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emails/availability")
    public ResponseEntity<Void> checkEmailAvailability(
            @Valid @RequestBody EmailReqDto dto
    ) {
        userService.checkEmailAvailability(dto.getEmail());
        return ResponseEntity.ok().build();
    }
}
