package com.group8.meetingall.controller;

import com.group8.meetingall.repository.TranslateResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingDeploy {

  @Autowired
  private TranslateResultRepository resultRepository;

  @RequestMapping("/hello")
  public String greet() {
    return "Hello guys! Have a Nice Day!" + resultRepository.getFileContent("3b24a035-3bf0-49fb-afe6-8c318829fd5a");
  }
}
