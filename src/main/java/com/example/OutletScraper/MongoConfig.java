package com.example.OutletScraper;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

// prod config
@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        return new SimpleMongoClientDatabaseFactory(mongoClient, "RefactorDb");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}