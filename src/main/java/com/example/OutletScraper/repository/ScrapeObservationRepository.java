package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.ScrapeObservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapeObservationRepository extends MongoRepository<ScrapeObservation, String> {
    Optional<ScrapeObservation> findByItemId(String id);
}
