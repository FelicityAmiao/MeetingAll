package com.group8.meetingall.controller;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.vo.MeetingRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketController {

    @MessageMapping("/updateMeeting")
    @SendTo("/topic/subscribeMeetingStatus")
    public MeetingRoom bookingMeeting(MeetingRoom room) {
        return room;
    }

    @MessageMapping("/report")
    @SendTo("/queue/reportGeneration")
    public MeetingRecordVo informReportCreated(MeetingRecordVo meetingRecordVo) {
        return meetingRecordVo;
    }

}
