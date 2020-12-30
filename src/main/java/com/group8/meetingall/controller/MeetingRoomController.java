package com.group8.meetingall.controller;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.service.IMeetingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.group8.meetingall.service.impl.MeetingRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/meetingRooms")
@RestController
public class MeetingRoomController {
    @Autowired
    IMeetingRoomService meetingRoomService;

    Logger logger = LoggerFactory.getLogger(MeetingRoomController.class);

    @Autowired
    MeetingRoomServiceImpl meetingRoomServiceImpl;

    @GetMapping
    public ResponseEntity<Object> queryAllMeetingRooms() {
        return new ResponseEntity<Object>(meetingRoomService.queryAllMeetingRooms(), HttpStatus.OK);
    }

    @GetMapping("option")
    public ResponseEntity<Object> queryAllRoomOptions() {
        return new ResponseEntity<Object>(meetingRoomService.getRoomOptions(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity updateMeetingRoomStartedStatus(@RequestBody MeetingRoom meetingRoom){
        meetingRoomServiceImpl.handleRoomDeviceStatus(Optional.of(meetingRoom).get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public void receiveMeetingRoomStatus(@PathVariable("roomId") String roomId, @RequestParam("status") String status) {
        meetingRoomServiceImpl.handleMeetingRoomStatus(roomId, status);
    }
}
