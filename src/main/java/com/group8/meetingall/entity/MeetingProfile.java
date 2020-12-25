package com.group8.meetingall.entity;

import com.group8.meetingall.vo.MeetingVo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document("meeting_profile")
public class MeetingProfile {
    private String userId;
    private String meetingId;
    private Double duration;
    private Integer language;
    private List<Integer> room;
    private String createTime;
    private String startTime;
    private String endTime;
    private boolean finished;
    private String reportAddress;
    private String audioAddress;
    private String status;
    private boolean delete;
//    1 means finished, 0 means not begin
}
