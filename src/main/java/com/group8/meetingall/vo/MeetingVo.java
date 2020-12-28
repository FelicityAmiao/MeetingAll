package com.group8.meetingall.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingVo {
    private String userId;
    private String meetingId;
    private Integer language;
    private List<Integer> room;
    private String createTime;
    private String startTime;
    private String endTime;
    private boolean isActive;
    private String status;
    private String subject;
}
