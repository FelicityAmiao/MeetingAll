package com.group8.meetingall.controller;

import com.group8.meetingall.dto.MeetingDto;
import com.group8.meetingall.entity.MeetingProfile;
import com.group8.meetingall.service.MyMeetingService;
import com.group8.meetingall.vo.MeetingRecordVo;
import com.group8.meetingall.vo.MeetingVo;
import com.itmuch.lightsecurity.jwt.UserOperator;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyMeetingController {
    @Autowired
    private MyMeetingService meetingService;
    private final UserOperator userOperator;

    @PostMapping
    private MeetingVo addMeeting(@RequestBody MeetingDto meetingDto) {
        String username = userOperator.getUser().getUsername();
        meetingDto.setUserId(username);
        return meetingService.upsertMeeting(meetingDto);
    }

    @GetMapping()
    private MeetingVo getMeeting() {
        String username = userOperator.getUser().getUsername();
        return meetingService.getActiveMeeting(username);
    }
    @PostMapping("{meetingId}")
    private Boolean finishMeeting(@PathVariable(value = "meetingId") String meetingId) {
        return meetingService.finishMeeting(meetingId);
    }

    @GetMapping("/report/{meetingId}")
    private MeetingVo generateReport(@PathVariable(value = "meetingId") String meetingId) {
        return meetingService.generateReport(meetingId);
    }

    @GetMapping("/meetingrecords")
    private List<MeetingRecordVo> getMeetingRecords() {
        String username = userOperator.getUser().getUsername();
        return meetingService.getMeetingRecords(username);
    }

    @PostMapping("/recording")
    private ResponseEntity<Object> recording(@RequestBody MeetingProfile meeting){
        return new ResponseEntity<Object>(meetingService.recording(meeting), HttpStatus.OK);
    }

    @PostMapping(value = "/upload/audio")
    public ResponseEntity<Object> saveVoiceRecord(@RequestParam("attachmentFile") MultipartFile uploadFile, @RequestParam("meetingId") String meetingId , @RequestParam("meetingSubject") String meetingSubject, HttpServletRequest request){
        return new ResponseEntity<Object>(meetingService.saveVoiceRecord(uploadFile, meetingId, meetingSubject), HttpStatus.OK);
    }
}
