package com.group8.meetingall.repository;

import com.alibaba.fastjson.JSON;
import com.group8.meetingall.entity.MeetingProfile;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<MeetingProfile> findAllMeetingsByUserId(String userId) {
        Document criteria = new Document();
        criteria.put("userId", userId);
        criteria.put("delete", false);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.find(query, MeetingProfile.class, "meeting_profile");
    }

    public MeetingProfile findMeetingByMeetingId(String meetingId) {
        Document criteria = new Document();
        criteria.put("meetingId", meetingId);
        criteria.put("delete", false);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.findOne(query, MeetingProfile.class, "meeting_profile");
    }

    public MeetingProfile findActiveMeetingByUserId(String userId) {
        Document criteria = new Document();
        criteria.put("userId", userId);
        criteria.put("active", true);
        criteria.put("delete", false);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.findOne(query, MeetingProfile.class, "meeting_profile");
    }

    public boolean upsertMeeting(MeetingProfile meetingProfile) {
        Document criteria = new Document();
        criteria.put("userId", meetingProfile.getUserId());
        criteria.put("meetingId", meetingProfile.getMeetingId());
        BasicQuery query = new BasicQuery(criteria);
        try {
            mongoTemplate.upsert(query, Update.fromDocument(Document.parse(JSON.toJSONString(meetingProfile))), "meeting_profile");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
