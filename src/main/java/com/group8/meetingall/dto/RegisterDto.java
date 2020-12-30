package com.group8.meetingall.dto;

import com.group8.meetingall.entity.User;
import lombok.Data;

@Data
public class RegisterDto {
    private User user;
    private boolean success;
    private String msg;
}
