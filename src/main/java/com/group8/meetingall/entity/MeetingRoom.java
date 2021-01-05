package com.group8.meetingall.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("meeting_rooms")
public class MeetingRoom {
    @Id
    private String id;
    private String roomId;
    private String office;
    private String room;
    private String currentStatus;
    @JsonProperty("isDeviceStarted")
    private boolean isDeviceStarted;
    public MeetingRoom () {}
    public MeetingRoom(String id, String office, String room, String currentStatus) {
        this.id = id;
        this.office = office;
        this.room = room;
        this.currentStatus = currentStatus;
    }
}