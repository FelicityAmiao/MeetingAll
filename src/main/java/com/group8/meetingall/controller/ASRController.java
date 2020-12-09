package com.group8.meetingall.controller;

import com.group8.meetingall.service.ASRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ASR")
public class ASRController {

    @Autowired
    ASRService asrService;
    @GetMapping(value="/trigger")
    public String triggerConvert(){
        asrService.startConvert();
        return "success";
    }
}
