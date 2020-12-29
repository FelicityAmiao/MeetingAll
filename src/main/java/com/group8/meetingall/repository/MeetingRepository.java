package com.group8.meetingall.repository;

import com.alibaba.fastjson.JSON;
import com.group8.meetingall.entity.MeetingProfile;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(MeetingRepository.class);

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
            logger.info(e.toString());
            return false;
        }
        return true;
    }

    public boolean findByIdAndUpdate(String id, Update update) {
        Document criteria = new Document();
        criteria.put("id", id);
        BasicQuery query = new BasicQuery(criteria);
        try {
            mongoTemplate.findAndModify(query, update, MeetingProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
