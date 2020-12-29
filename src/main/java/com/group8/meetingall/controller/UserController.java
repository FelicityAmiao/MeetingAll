package com.group8.meetingall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group8.meetingall.entity.User;
import com.group8.meetingall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        User user = userService.login(username, password);
        HttpStatus status = user.getUserId() == null ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
        return new ResponseEntity<>(user, status);
    }

    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        User user = userService.regist(username, password);
        HttpStatus status = user.getUserId() == null ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;
        return new ResponseEntity<>(user, status);
    }

    @GetMapping("/{username}")
    public boolean verifyUsername(@PathVariable String username){
        return userService.verifyUsername(username);
    }

}
