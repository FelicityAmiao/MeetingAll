package com.group8.meetingall.schedule;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.entity.MeetingRoomScheduler;
import com.group8.meetingall.service.IMeetingRoomService;
import com.group8.meetingall.service.SchedulerService;
import com.group8.meetingall.utils.IOTConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

import static com.group8.meetingall.constant.MeetingRoomConstant.*;

@Component
public class SchedulerJob {

    @Autowired
    IMeetingRoomService iMeetingRoomService;

    @Autowired
    SchedulerService schedulerService;

    @Scheduled(cron = "0/30 * * * * *")
    public void currentStatusSchedule() {
        System.out.println("Start current status monitor");
        System.out.println(new Date());
        List<MeetingRoom> meetingRooms = iMeetingRoomService.queryAllMeetingRooms();
        meetingRooms.forEach(this::currentStatusMonitor);
    }

    @Transactional(rollbackFor = Exception.class)
    public void currentStatusMonitor(MeetingRoom meetingRoom) {
        if (!ObjectUtils.isEmpty(meetingRoom)) {
            MeetingRoomScheduler currentRoomScheduler = schedulerService.queryScheduleByRoomName(meetingRoom.getRoom());
            if (!ObjectUtils.isEmpty(currentRoomScheduler)
                    && schedulerService.isMoreThan5Minutes(currentRoomScheduler.getLastDateTime())) {
                schedulerService.upsertSchedule(meetingRoom.getRoom());
                schedulerService.updateStatusById(meetingRoom.getId());
            }
        }
    }
}
