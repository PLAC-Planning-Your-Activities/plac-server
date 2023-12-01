package com.plac.service.user;

import com.plac.domain.User;
import com.plac.dto.request.user.UserReqDto;
import com.plac.dto.response.user.UserResDto;

import java.util.List;

public interface UserService {
    UserResDto signUp(UserReqDto.CreateUser reqDto);

    User findUser(Long userId);

    void deleteUser();

    void deleteUser(Long userId);

    List<UserResDto> findAll();

    void checkEmailAvailability(String email);
}
