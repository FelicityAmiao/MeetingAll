package com.group8.meetingall.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("meeting_room_scheduler")
public class MeetingRoomScheduler {
    @Id
    private String id;
    private String roomName;
    private Date lastDateTime;


    public MeetingRoomScheduler(String roomName, Date lastDateTime) {
        this.roomName = roomName;
        this.lastDateTime = lastDateTime;
    }
}
