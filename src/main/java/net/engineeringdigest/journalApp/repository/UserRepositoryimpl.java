package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryimpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {

        Query query = new Query();

        query.addCriteria(new Criteria().andOperator(
                Criteria.where("email").exists(true),
                Criteria.where("email").ne(null),
                Criteria.where("email").ne(""),
                Criteria.where("email").regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
                Criteria.where("sentimentAnalysis").is(true)
        ));

        System.out.println("Mongo Query: " + query.getQueryObject().toJson());

        return mongoTemplate.find(query, User.class);
    }
}