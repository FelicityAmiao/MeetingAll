package com.group8.meetingall.entity;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("meeting_room_config")
public class MeetingRoomConfig {
  @Id
  private String id;
  private String key;
  private String value;
}
