package com.group8.meetingall.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {

  Logger logger = LoggerFactory.getLogger(ArduinoController.class);

  @GetMapping("/status")
  public String receiveMeetingStatus(@RequestParam("roomId") String roomId, @RequestParam("value") String value) {
//    Optional<MeetingStatusDto> op = Optional.ofNullable(meetingStatusDto);
    String responseData = "Hi all, get data from Arduino: roomId = " + roomId + ", value = " + value;
    logger.info("testing : {}" + responseData);
    return responseData;
  }
}
