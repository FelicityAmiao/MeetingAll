package com.group8.meetingall.controller;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.repository.TranslateResultRepository;
import com.group8.meetingall.utils.JsonUtils;
import com.group8.meetingall.vo.MeetingRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingDeploy {

    @Autowired
    private TranslateResultRepository resultRepository;

    @Autowired
    WebSocketController webSocketController;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @PostMapping("/websocket")
    public String testWebSocket(@RequestBody MeetingRoom meetingRoom) {
        String s = JsonUtils.toJson(meetingRoom);
        simpMessageSendingOperations.convertAndSend("/topic/subscribeMeetingStatus", s);
        return s;
    }

    @RequestMapping("/hello")
    public String greet() {
        return "Hello guys! Have a Nice Day!" + resultRepository.getFileContent("3b24a035-3bf0-49fb-afe6-8c318829fd5a");
    }

    @RequestMapping()
    public String test() {
        return "Hello guys! Have a Nice Day! Testing auto build of jenkins https1";
    }

}
