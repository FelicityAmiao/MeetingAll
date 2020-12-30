package com.group8.meetingall.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("auth_information")
public class AuthInformation {
    @Id
    private String username;
    private Integer authCode;
    private String deadLine;
}