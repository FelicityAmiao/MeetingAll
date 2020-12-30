package com.group8.meetingall.repository;

import com.group8.meetingall.entity.MeetingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository  extends MongoRepository<MeetingRoom, String> {
  MeetingRoom findByRoomId(String roomId);

  @Override
  List<MeetingRoom> findAll();
}
