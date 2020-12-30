package com.group8.meetingall.repository;

import com.group8.meetingall.entity.MeetingRoomScheduler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<MeetingRoomScheduler, String> {
    MeetingRoomScheduler findByRoomName(String roomName);
}
