package com.group8.meetingall.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingVo {
    private String id;
    private String userId;
    private String meetingId;
    private Integer language;
    private List<String> room;
    private String createTime;
    private String startTime;
    private String startDate;
    private String duration;
    private String endTime;
    private boolean isActive;
    private String status;
    private String subject;
    private String reportAddress;
}
