package com.group8.meetingall.controller;

import com.group8.meetingall.dto.MeetingDto;
import com.group8.meetingall.entity.MeetingProfile;
import com.group8.meetingall.service.MyMeetingService;
import com.group8.meetingall.vo.MeetingRecordVo;
import com.group8.meetingall.vo.MeetingVo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RequestMapping("/myMeeting")
@RestController
public class MyMeetingController {
    @Autowired
    private MyMeetingService meetingService;

    @PostMapping
    private MeetingVo addMeeting(@RequestBody MeetingDto meetingDto) {
        return meetingService.upsertMeeting(meetingDto);
    }

    @GetMapping("/{userId}")
    private MeetingVo getMeeting(@PathVariable(value = "userId") String userId) {
        return meetingService.getActiveMeeting(userId);
    }

    @GetMapping("/report/{meetingId}")
    private MeetingVo generateReport(@PathVariable(value = "meetingId") String meetingId) {
        return meetingService.generateReport(meetingId);
    }

    @GetMapping("/meetingrecords/{user}")
    private List<MeetingRecordVo> getMeetingRecords(@PathVariable(value = "user") String user) {
        return meetingService.getMeetingRecords(user);
    }

    @PostMapping("/recording")
    private ResponseEntity<Object> recording(@RequestBody MeetingProfile meeting){
        return new ResponseEntity<Object>(meetingService.recording(meeting), HttpStatus.OK);
    }

    @PostMapping(value = "/upload/audio")
    public ResponseEntity<Object> saveVoiceRecord(@RequestParam("attachmentFile") MultipartFile uploadFile, @RequestParam("meetingId") String meetingId , HttpServletRequest request){
        return new ResponseEntity<Object>(meetingService.saveVoiceRecord(uploadFile, meetingId), HttpStatus.OK);
    }
}
