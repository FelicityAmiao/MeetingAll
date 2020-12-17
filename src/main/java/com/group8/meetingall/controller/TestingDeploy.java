package com.group8.meetingall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingDeploy {

  @RequestMapping("/hello")
  public String greet() {
    return "Hello guys! Have a Nice Day!";
  }
}
