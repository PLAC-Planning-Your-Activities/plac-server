package com.plac.controller;

import com.plac.domain.Message;
import com.plac.dto.request.user.UserReqDto;
import com.plac.dto.response.user.UserResDto;
import com.plac.service.UserService;
import com.plac.util.SecurityContextHolderUtil;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return buildResponseEntity(HttpStatus.OK, "success");
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

        return buildResponseEntity(HttpStatus.OK, "success");
    }


    /**
     * TEST URL : 테스트가 끝나면 필수적으로 지워야 함.
     * */
    @Operation(summary = "로그인 테스트 api", description = "로그인 상태에서, 해당 유저의 정보를 출력.")
    @GetMapping("/test1")
    public ResponseEntity<?> TestApi() throws Exception {
        Long userId = SecurityContextHolderUtil.getUserId();
        UserResDto userResDto = UserResDto.of(userService.findOne(userId));

        return buildResponseEntity(userResDto, HttpStatus.OK, "success");
    }

    @Operation(summary = "로그인 테스트 api", description = "로그인 상태에서, 등록된 모든 유저정보 출력")
    @GetMapping("/test2")
    public ResponseEntity<?> searchAll(){
        List<UserResDto> result = userService.findAll();

        return buildResponseEntity(result, HttpStatus.OK, "success");
    }

    private ResponseEntity<Message> buildResponseEntity(HttpStatus status, String msg) {
        Message message = Message.builder()
                .status(status)
                .message(msg)
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }

    private ResponseEntity<Message> buildResponseEntity(Object data, HttpStatus status, String msg) {
        Message message = Message.builder()
                .data(data)
                .status(status)
                .message(msg)
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }
}
