package com.group8.meetingall.controller;

import com.group8.meetingall.service.IMeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/meetingRooms")
@RestController
public class MeetingRoomController {
    @Autowired
    IMeetingRoomService meetingRoomService;

    @GetMapping()
    public ResponseEntity<Object> queryAllMeetingRooms() {
        return new ResponseEntity<Object>(meetingRoomService.queryAllMeetingRooms(), HttpStatus.OK);
    }
}
