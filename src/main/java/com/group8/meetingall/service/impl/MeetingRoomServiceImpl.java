package com.group8.meetingall.service.impl;

import com.group8.meetingall.entity.MeetingRoom;
import com.group8.meetingall.repository.MeetingRoomRepository;
import com.group8.meetingall.service.IMeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MeetingRoomServiceImpl implements IMeetingRoomService {
    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Override
    public List<MeetingRoom> queryAllMeetingRooms() {
        Sort sort = Sort.by(Sort.Direction.ASC, "office");
        return meetingRoomRepository.findAll(sort);
    }
}
