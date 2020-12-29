package com.group8.meetingall.repository;
import com.group8.meetingall.entity.MeetingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomRepository  extends MongoRepository<MeetingRoom, String> {
  MeetingRoom findByRoomId(String roomId);
}
