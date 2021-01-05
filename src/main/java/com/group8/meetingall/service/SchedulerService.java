package com.group8.meetingall.service;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.entity.MeetingRoomConfig;
import com.group8.meetingall.entity.MeetingRoomScheduler;
import com.group8.meetingall.repository.MeetingRoomConfigRepository;
import com.group8.meetingall.repository.MeetingRoomRepository;
import com.group8.meetingall.repository.ScheduleRepository;
import com.group8.meetingall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static com.group8.meetingall.constant.MeetingRoomConstant.IDLE_STATUS;

@Service
public class SchedulerService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Autowired
    MeetingRoomConfigRepository configRepository;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public MeetingRoomScheduler queryScheduleByRoomName(String roomName) {
        return scheduleRepository.findByRoomName(roomName);
    }

    public void updateStatusById(String id) {
        if (StringUtils.isNoneBlank(id)) {
            MeetingRoom meetingRoom = meetingRoomRepository.findById(id).get();
            if (!ObjectUtils.isEmpty(meetingRoom)) {
                meetingRoom.setCurrentStatus(IDLE_STATUS);
                meetingRoom.setDeviceStarted(false);
                meetingRoomRepository.save(meetingRoom);
                simpMessageSendingOperations.convertAndSend("/topic/subscribeMeetingStatus", JsonUtils.toJson(meetingRoom));
            }
        }
    }

    public boolean isMoreThan5Minutes(Date lastDateTime) {
        MeetingRoomConfig meetingRoomConfig = configRepository.findByKey("Monitor_Scheduler_Time");
        long lastDateTimeSecond = lastDateTime.getTime();
        long currentDateTimeSecond = new Date().getTime();
        return currentDateTimeSecond - lastDateTimeSecond > Long.parseLong(meetingRoomConfig.getValue());
    }

    public void upsertSchedule(String roomName) {
        MeetingRoomScheduler currentMeetingRoomScheduler = scheduleRepository.findByRoomName(roomName);
        if (!ObjectUtils.isEmpty(currentMeetingRoomScheduler)) {
            currentMeetingRoomScheduler.setLastDateTime(new Date());
            scheduleRepository.save(currentMeetingRoomScheduler);
        } else {
            MeetingRoomScheduler meetingRoomScheduler = new MeetingRoomScheduler(roomName, new Date());
            scheduleRepository.save(meetingRoomScheduler);
        }
    }
}
