package com.group8.meetingall.vo;

import lombok.Data;

import java.util.List;

@Data
public class MeetingRecordVo {
    private List<Integer> room;
    private Integer language;
    private String date;
    private String status;
    private String reportAddress;
    private String audioAddress;
    private String subject;
}
