package com.group8.meetingall.repository;

import com.group8.meetingall.entity.TranslateResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TranslateResultRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    public TranslateResultEntity saveTranslateResult(String translateResult) {
        TranslateResultEntity translateResultEntity = TranslateResultEntity.builder()
                .UUID(UUID.randomUUID().toString())
                .translateResult(translateResult)
                .build();
        mongoTemplate.save(translateResultEntity);
        return translateResultEntity;
    }

    public String getFileContent(String uuid) {
        Query query = new Query(Criteria.where("UUID").is(uuid));
        TranslateResultEntity translateResultEntity = mongoTemplate.findOne(query, TranslateResultEntity.class);
        return translateResultEntity.getTranslateResult();
    }
}
