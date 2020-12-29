package com.group8.meetingall.repository;

import com.alibaba.fastjson.JSON;
import com.group8.meetingall.entity.User;
import com.group8.meetingall.utils.EncryptUtil;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.List;

@Component
public class UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findUserByUsername(String username){
        Document criteria = new Document();
        criteria.put("username", username);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.find(query, User.class, "user");
    }

    public User upsertUser(User user){
        Document criteria = new Document();
        criteria.put("username", user.getUsername());
        criteria.put("password", user.getUsername());
        BasicQuery query = new BasicQuery(criteria);
        UpdateResult result = mongoTemplate.upsert(query, Update.fromDocument(Document.parse(JSON.toJSONString(user))), "user");
        user.setUserId(String.valueOf(result.getUpsertedId()));
        return user;
    }
}
