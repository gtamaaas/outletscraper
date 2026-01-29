package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Alert.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {

    <Optional> Alert findByItemId(String itemID);

    List<Alert> findAllByItemId(String id);
}
