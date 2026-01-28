package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Article.ScrapeObservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScrapeObservationRepository extends MongoRepository<ScrapeObservation, String> {
}
