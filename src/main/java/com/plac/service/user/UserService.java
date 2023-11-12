package com.plac.service.user;

import com.plac.domain.User;
import com.plac.dto.request.user.UserReqDto;
import com.plac.dto.response.user.UserResDto;

import java.util.List;

public interface UserService {
    public UserResDto signUp(UserReqDto.CreateUser reqDto);
    public User findUser(Long userId);
    public void deleteUser();
    public void deleteUser(Long userId);
    public List<UserResDto> findAll();
}
