package com.group8.meetingall.controller;

import com.group8.meetingall.service.IMeetingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/meetingRooms")
@RestController
public class MeetingRoomController {
    @Autowired
    IMeetingRoomService meetingRoomService;

    Logger logger = LoggerFactory.getLogger(MeetingRoomController.class);


    @GetMapping
    public ResponseEntity<Object> queryAllMeetingRooms() {
        return new ResponseEntity<Object>(meetingRoomService.queryAllMeetingRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public String sendDeviceStatusToArduino(@RequestParam("deviceStatus")String deviceStatus) {
        logger.info("===incoming===");
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://192.168.43.123:80/update?deviceStatus=" + deviceStatus, String.class);
//        ResponseEntity<String> response = restTemplate.getForEntity("http://146.222.43.156:8077/api/arduino/test?roomId=" + deviceStatus, String.class);
        logger.info("statusCode: " + response.getStatusCode());
        logger.info("statusCodeValue: " + response.getStatusCodeValue());
        logger.info("body: " + response.getBody());
        return response.getStatusCodeValue() == 200 ? "success" : "failed";
    }
}
