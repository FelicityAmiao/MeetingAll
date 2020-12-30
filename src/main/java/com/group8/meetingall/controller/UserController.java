package com.group8.meetingall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group8.meetingall.dto.RegisterDto;
import com.group8.meetingall.entity.User;
import com.group8.meetingall.service.UserService;
import com.group8.meetingall.utils.EmailUtil;
import com.group8.meetingall.vo.PasswordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private String subject = "Meeting All注册";

    @PostMapping("/login")
    public ResponseEntity<Object> login(){
        User user = userService.getUser();
        HttpStatus status = user.getUserId() == null ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
        return new ResponseEntity<>(user, status);
    }

    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String authCode = jsonObject.getString("authCode");
        RegisterDto registerDto = userService.regist(username, password,Integer.valueOf(authCode));
        return new ResponseEntity<>(registerDto, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public boolean verifyUsername(@PathVariable String username){
        return userService.verifyUsername(username);
    }

    @PostMapping("/authcode")
    public boolean sendAuthCode(@RequestBody String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String username = jsonObject.getString("username");
        Random rand = new Random();
        Integer authCode = rand.nextInt(899999)+100000;
        String text = "验证码: "+authCode+" ,用于Meeting All验证,有效期15分钟。泄露有风险，请勿转发。";
        boolean isSuccess = userService.saveAuthCode(username,authCode);
        if(isSuccess){
            isSuccess = EmailUtil.sendEmail(username, subject, text);
        }
        return isSuccess;
    }

    @PostMapping("/password")
    public ResponseEntity<Object> changePassword(@RequestBody String jsonString){
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String username = jsonObject.getString("username");
        String originPassword = jsonObject.getString("originPassword");
        String newPassword = jsonObject.getString("newPassword");
        PasswordVo PasswordVo = userService.changePassword(username, originPassword,newPassword);
        return new ResponseEntity<>(PasswordVo, HttpStatus.OK);
    }
}
