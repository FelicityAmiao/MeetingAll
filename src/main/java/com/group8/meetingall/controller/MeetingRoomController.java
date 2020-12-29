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

//    @GetMapping("/{id}")
//    public String sendDeviceStatusToArduino(@RequestParam("deviceStatus")String deviceStatus) {
//        logger.info("===incoming===");
//        RestTemplate restTemplate=new RestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity("http://192.168.43.123:80/update?deviceStatus=" + deviceStatus, String.class);
////        ResponseEntity<String> response = restTemplate.getForEntity("http://146.222.43.156:8077/api/arduino/test?roomId=" + deviceStatus, String.class);
//        logger.info("statusCode: " + response.getStatusCode());
//        logger.info("statusCodeValue: " + response.getStatusCodeValue());
//        logger.info("body: " + response.getBody());
//        return response.getStatusCodeValue() == 200 ? "success" : "failed";
//    }

    @PostMapping
    public ResponseEntity updateMeetingRoomStartedStatus(@RequestBody MeetingRoom meetingRoom){
        meetingRoomServiceImpl.updateStartedStatusByRoomId(Optional.of(meetingRoom).get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public void receiveMeetingRoomStatus(@PathVariable("roomId") String roomId, @RequestParam("status") String status) {
        meetingRoomServiceImpl.handleMeetingRoomStatus(roomId, status);
    }
}
