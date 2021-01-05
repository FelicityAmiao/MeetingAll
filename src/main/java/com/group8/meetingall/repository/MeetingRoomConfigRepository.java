package com.group8.meetingall.repository;

import com.group8.meetingall.entity.MeetingRoomConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomConfigRepository extends MongoRepository<MeetingRoomConfig, String> {
  MeetingRoomConfig findByKey(String key);
}
