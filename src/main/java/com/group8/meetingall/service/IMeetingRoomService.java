package com.group8.meetingall.service;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.vo.MeetingRoomVo;

import java.util.List;

public interface IMeetingRoomService {
    List<MeetingRoom> queryAllMeetingRooms();

    void updateStartedStatusByRoomId(MeetingRoom meetingRoom);

    void handleMeetingRoomStatus(String roomId, String status);
}
