package com.example.technical_challenge.service.user;

import com.example.technical_challenge.dto.UserDto;

import java.util.List;

public interface IUserService {

    UserDto createUser (UserDto userDto);

    UserDto getUser (Integer userId);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Integer userId);

    List<UserDto> getAllUsers();
}
