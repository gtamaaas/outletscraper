package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByItemId(String id);
}

