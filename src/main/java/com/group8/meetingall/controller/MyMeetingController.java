package com.group8.meetingall.controller;

import com.group8.meetingall.dto.MeetingDto;
import com.group8.meetingall.service.MyMeetingService;
import com.group8.meetingall.vo.MeetingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/myMeeting")
@RestController
public class MyMeetingController {
    @Autowired
    private MyMeetingService meetingService;

    @PostMapping
    private List<MeetingVo> addMeeting(@RequestBody MeetingDto meetingDto){
        return meetingService.upsertMeeting(meetingDto);
    }

    @GetMapping("/{userId}")
    private List<MeetingVo> getAllMeetings(@PathVariable(value = "userId") String userId){
        return meetingService.findAllMeetingsByUserId(userId);
    }

    @DeleteMapping("/{userId}/{meetingId}")
    private List<MeetingVo> deleteMeetings(@PathVariable(value = "userId") String userId,
                                           @PathVariable(value = "meetingId") String meetingId){
        return meetingService.deleteMeeting(userId, meetingId);
    }
}
