package com.group8.meetingall.repository;

import com.alibaba.fastjson.JSON;
import com.group8.meetingall.entity.AuthInformation;
import com.group8.meetingall.entity.User;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User findUserByUsername(String username){
        Document criteria = new Document();
        criteria.put("username", username);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.findOne(query, User.class, "user");
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

    public boolean upsertAuthCode(AuthInformation authInformation){
        boolean isSuccess = true;
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(authInformation.getUsername()));
        Update update = new Update();
        update.set("username",authInformation.getUsername());
        update.set("authCode",authInformation.getAuthCode());
        update.set("deadLine",authInformation.getDeadLine());
        try {
            mongoTemplate.upsert(query, update, AuthInformation.class, "auth_information");
        }catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    public List<AuthInformation> findAuthCode(String username,Integer authCode){
        Document criteria = new Document();
        criteria.put("username", username);
        criteria.put("authCode", authCode);
        BasicQuery query = new BasicQuery(criteria);
        return mongoTemplate.find(query, AuthInformation.class, "auth_information");
    }
}
