package com.plac.domain.user.controller;

import com.plac.domain.user.dto.UserReqDto;
import com.plac.domain.user.service.UserService;
import com.plac.dto.request.email.EmailReqDto;
import com.plac.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "유저 회원가입 api", description = "유저 하나를 생성한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 생성 완료"),
                    @ApiResponse(responseCode = "400", description = "유저 이메일이 이미 존재할 때"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @PostMapping("/one")
    public ResponseEntity<?> signUp(@RequestBody UserReqDto.CreateUser req) throws Exception {
        userService.signUp(req);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @Operation(summary = "유저 회원 탈퇴(삭제) api", description = "현재 로그인 된 유저를 삭제한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 삭제 완료"),
                    @ApiResponse(responseCode = "401", description = "토큰 만료,"),
                    @ApiResponse(responseCode = "401", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @DeleteMapping("/one")
    public ResponseEntity<?> deleteUser() throws Exception {
        userService.deleteUser();

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
