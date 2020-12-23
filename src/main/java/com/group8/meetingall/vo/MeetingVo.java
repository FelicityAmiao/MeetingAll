package com.group8.meetingall.vo;

import com.group8.meetingall.entity.MeetingProfile;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class MeetingVo {
    private String userId;
    private String meetingId;
    private Double duration;
    private Integer language;
    private List<Integer> room;
    private List<LocalDateTime> time;
    private String createTime;
    private String startTime;
    private String endTime;
    private boolean finished;
    private String reportAddress;
}
