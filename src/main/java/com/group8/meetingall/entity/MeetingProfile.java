package com.group8.meetingall.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("meeting_profile")
public class MeetingProfile {
    @Id
    private String id;
    private String userId;
    private String meetingId;
    private Double duration;
    private Integer language;
    private List<Integer> room;
    private String createTime;
    private String startDate;
    private String startTime;
    private String endTime;
    private String reportAddress;
    private String audioAddress;
    private String status;
    private boolean delete;
    private boolean active;
    private String subject;
}
