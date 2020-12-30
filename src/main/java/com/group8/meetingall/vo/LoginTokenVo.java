package com.group8.meetingall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginTokenVo {
    private String token;
    private String username;
}
