package com.plac.controller;

import com.plac.domain.Message;
import com.plac.dto.request.UserReqDto;
import com.plac.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 생성 api", description = "유저 하나를 생성한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 생성 완료"),
                    @ApiResponse(responseCode = "400", description = "유저 이메일이 이미 존재할 때"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody UserReqDto.CreateUser req) throws Exception {

        userService.register(req);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }

    @Operation(summary = "유저 삭제 api", description = "유저 하나를 삭제한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 삭제 완료"),
                    @ApiResponse(responseCode = "401", description = "토큰 만료,"),
                    @ApiResponse(responseCode = "401", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @DeleteMapping("/one")
    public ResponseEntity<?> deleteOne() throws Exception {
        userService.deleteUser();

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }
}
