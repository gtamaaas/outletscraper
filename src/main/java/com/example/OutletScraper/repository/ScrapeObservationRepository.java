package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Item.ScrapeObservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScrapeObservationRepository extends MongoRepository<ScrapeObservation, String> {
    List<ScrapeObservation> findAllByItemId(String itemId);

}
