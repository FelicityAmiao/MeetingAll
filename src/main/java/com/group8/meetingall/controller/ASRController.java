package com.group8.meetingall.controller;

import com.group8.meetingall.service.ASRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/ASR")
public class ASRController {

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    ASRService asrService;

    @GetMapping(value="/convert")
    public String convert() throws UnknownHostException {
        String UUID = asrService.convert();
        InetAddress inetAddress= InetAddress.getLocalHost();
        return inetAddress.getHostName() + ":" + serverPort + "/api/ASR/getTranslateResultFile?uuid=" + UUID;
    }

    @GetMapping(value="/getTranslateResultFile")
    public String getTranslateResultFile(@RequestParam String uuid)  {
        return asrService.getTranslateResultFile(uuid);
    }
}
