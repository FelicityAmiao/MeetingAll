package com.group8.meetingall.controller;

import com.group8.meetingall.dto.MeetingStatusDto;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {

  @PostMapping("/status")
  public String receiveMeetingStatus(@RequestBody MeetingStatusDto meetingStatusDto) {
    Optional<MeetingStatusDto> op = Optional.ofNullable(meetingStatusDto);
    String responseData = "Hi all, get data from Arduino: roomId = " + op.get().getRoomId() + ", status = " + op.get().getStatus();
    System.out.println("[info]: " + responseData);
    return responseData;
  }
}
