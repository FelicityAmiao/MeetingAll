package com.group8.meetingall.controller;


import com.group8.meetingall.dto.UserDto;
import com.group8.meetingall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("auth")
    public String loginReturnToken(@RequestBody UserDto loginUser) {
        return userService.auth(loginUser);
    }
}
