package com.group8.meetingall.repository;

import com.alibaba.fastjson.JSON;
import com.group8.meetingall.entity.MeetingProfile;
import com.group8.meetingall.vo.MeetingRecordVo;
import com.group8.meetingall.vo.MeetingVo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

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

    public MeetingProfile findMeetingByUserIdAndMeetingId(String userId, String meetingId) {
        Document criteria = new Document();
        criteria.put("userId", userId);
        criteria.put("meetingId", meetingId);
        criteria.put("delete", false);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.findOne(query, MeetingProfile.class, "meeting_profile");
    }

    public boolean addMeeting(MeetingProfile meetingProfile) {
        try {
            mongoTemplate.insert(meetingProfile, "meeting_profile");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    @Transactional
    public boolean removeMeeting(String userId, String meetingId) {
        Document criteria = new Document();
        criteria.put("userId", userId);
        criteria.put("meetingId", meetingId);
        BasicQuery query = new BasicQuery(criteria);
        MeetingProfile meetingProfile = mongoTemplate.findOne(query, MeetingProfile.class, "meeting_profile");
        if (nonNull(meetingProfile)) {
            meetingProfile.setDelete(true);
            try {
                mongoTemplate.upsert(query, Update.fromDocument(Document.parse(JSON.toJSONString(meetingProfile))), "meeting_profile");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
