package com.group8.meetingall.controller;

import com.group8.meetingall.dto.MeetingStatusDto;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {

  @GetMapping("/status")
  public String receiveMeetingStatus(@RequestParam("roomId") String roomId, @RequestParam("value") String value) {
//    Optional<MeetingStatusDto> op = Optional.ofNullable(meetingStatusDto);
    String responseData = "Hi all, get data from Arduino: roomId = " + roomId + ", value = " + value;
    System.out.println("[info]: " + responseData);
    return responseData;
  }
}
