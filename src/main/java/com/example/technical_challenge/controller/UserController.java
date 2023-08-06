package com.example.technical_challenge.controller;


import com.example.technical_challenge.db.model.User;
import com.example.technical_challenge.dto.UserDto;
import com.example.technical_challenge.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/getUser")
    public UserDto getUser(@RequestBody Integer userId)
    {
        return userService.getUser(userId);
    }

    @PostMapping("/createUser")
    public UserDto createUser(@RequestBody UserDto userDto)
    {
        return userService.createUser(userDto);
    }

    @PutMapping("/updateUser")
    public UserDto updateUser(@RequestBody UserDto userDto)
    {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/deleteUser")
    public boolean deleteUser(@RequestBody Integer userId){
        userService.deleteUser(userId);
        return true;
    }

    @GetMapping("/getAllUsers")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
}
