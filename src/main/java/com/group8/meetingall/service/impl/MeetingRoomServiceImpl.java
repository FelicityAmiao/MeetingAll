package com.group8.meetingall.service.impl;

import com.group8.meetingall.controller.ArduinoController;
import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.repository.MeetingRoomRepository;
import com.group8.meetingall.service.IMeetingRoomService;
import com.group8.meetingall.service.SchedulerService;
import com.group8.meetingall.utils.IOTConnectUtil;
import com.group8.meetingall.utils.JsonUtils;
import com.group8.meetingall.vo.Children;
import com.group8.meetingall.vo.RoomOptionVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.group8.meetingall.constant.MeetingRoomConstant.*;

@Service
public class MeetingRoomServiceImpl implements IMeetingRoomService {
  Logger logger = LoggerFactory.getLogger(MeetingRoomServiceImpl.class);

  @Autowired
  MeetingRoomRepository meetingRoomRepository;

  @Autowired
  private SimpMessageSendingOperations simpMessageSendingOperations;

  @Autowired
  SchedulerService schedulerService;

  @Override
  public List<MeetingRoom> queryAllMeetingRooms() {
    Sort sort = Sort.by(Sort.Direction.ASC, "office");
    return meetingRoomRepository.findAll(sort);
  }

  @Override
  public void handleRoomDeviceStatus(MeetingRoom meetingRoom) {
    if (StringUtils.isNoneBlank(meetingRoom.getId())) {
      String deviceStatus = meetingRoom.isDeviceStarted() ? DEVICE_START_STATUS : DEVICE_STOP_STATUS;
      ResponseEntity<String> response = IOTConnectUtil.sendDeviceStatusToIOT(deviceStatus, meetingRoom.getRoom());
      if (response.getStatusCodeValue() == 200) {
        meetingRoomRepository.save(meetingRoom);
      }
    }
  }

  @Override
  public void handleMeetingRoomStatus(String roomId, String status) {
    if (StringUtils.isNoneBlank(roomId, status)) {
      MeetingRoom meetingRoom = meetingRoomRepository.findByRoomId(roomId);
      schedulerService.upsertSchedule(meetingRoom.getRoom());
      if (isForUsingStatus(status, meetingRoom)) {
        ResponseEntity<String> response = IOTConnectUtil.sendDeviceStatusToIOT(DEVICE_START_STATUS, meetingRoom.getRoom());
        if (response.getStatusCodeValue() == 200) {
          meetingRoom.setCurrentStatus(status);
          meetingRoom.setDeviceStarted(true);
          meetingRoomRepository.save(meetingRoom);
          simpMessageSendingOperations.convertAndSend("/topic/subscribeMeetingStatus", JsonUtils.toJson(meetingRoom));
        }
      }
      logger.info("MeetingRoom [" + meetingRoom.getOffice() + "-" + meetingRoom.getRoom() + "] is using now.");
    }
  }

  private boolean isForUsingStatus(String status, MeetingRoom meetingRoom) {
    return IDLE_STATUS.equals(meetingRoom.getCurrentStatus()) && USING_STATUS.equals(status);
  }

//  private boolean isForIdleStatus(String status, MeetingRoom meetingRoom) {
//    return USING_STATUS.equals(meetingRoom.getCurrentStatus()) && IDLE_STATUS.equals(status);
//  }

    @Override
    public List<RoomOptionVo> getRoomOptions() {
        List<RoomOptionVo> roomOptions = new ArrayList<>();
        List<MeetingRoom> rooms = meetingRoomRepository.findAll();
        Map<String, List<MeetingRoom>> officeMap = rooms.stream().collect(Collectors.groupingBy(MeetingRoom::getOffice));
        for (Map.Entry<String, List<MeetingRoom>> entry : officeMap.entrySet()) {
            RoomOptionVo roomOptionVo = new RoomOptionVo(entry.getKey());
            List<Children> children = entry.getValue().stream().map(meetingRoom -> new Children(meetingRoom.getRoom())).collect(Collectors.toList());
            roomOptionVo.setChildren(children);
            roomOptions.add(roomOptionVo);
        }
        return roomOptions;
    }
}
