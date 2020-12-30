package com.group8.meetingall.controller;

import com.group8.meetingall.entity.MeetingRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class RoomUpdateController {

    @MessageMapping("/updateMeeting")
    @SendTo("/topic/subscribeMeetingStatus")
    public MeetingRoom bookingMeeting(MeetingRoom room) {
        return room;
    }

}
