package com.group8.meetingall.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class IOTConnectUtil {
  public static final String HTTP_IOT_OPEN_URL = "http://luanyuan123.oicp.io";

  private static Map<String, String> routerMap = new HashMap<>();
  static {
    routerMap.put("Mars", HTTP_IOT_OPEN_URL);
  }

  public static ResponseEntity<String> sendDeviceStatusToIOT(String deviceStatus, String roomName) {
    RestTemplate restTemplate = new RestTemplate();
    String basicUrl = getBasicUrlByName(roomName);
    return restTemplate.getForEntity(basicUrl + "/update?deviceStatus=" + deviceStatus, String.class);
  }

  private static String getBasicUrlByName(String roomName) {
    return null != routerMap.get(roomName) ? routerMap.get(roomName) : routerMap.get("Mars");
  }

}

