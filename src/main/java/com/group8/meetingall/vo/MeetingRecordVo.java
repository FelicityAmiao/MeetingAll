package com.group8.meetingall.vo;

import lombok.Data;

import java.util.List;

@Data
public class MeetingRecordVo {
    private List<String> room;
    private Integer language;
    private String status;
    private String reportAddress;
    private String audioAddress;
    private String subject;
    private String duration;
    private String startDate;
    private String startTime;
    private String endTime;
    private String meetingId;
    private String createTime;
}
