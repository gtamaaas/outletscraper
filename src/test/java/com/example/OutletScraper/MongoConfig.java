package com.example.OutletScraper;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

// test config
@Configuration
@Profile("test")
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        MongoClient mongoClient =
                MongoClients.create("mongodb://127.0.0.1:27017");
        return new SimpleMongoClientDatabaseFactory(
                mongoClient, "RefactorDb_test");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}
