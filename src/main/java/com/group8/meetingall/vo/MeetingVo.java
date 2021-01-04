package com.group8.meetingall.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingVo {
    private String userId;
    private String meetingId;
    private String duration;
    private Integer language;
    private List<String> room;
    private String createTime;
    private String startDate;
    private String startTime;
    private String endTime;
    private String reportAddress;
    private String status;
    private String subject;
}
