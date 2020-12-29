package com.group8.meetingall.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("meeting_rooms")
public class MeetingRoom {
    @Id
    private String id;
    private String office;
    private String room;
    private String currentStatus;
    private boolean isDeviceStarted;
}