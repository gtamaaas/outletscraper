package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.SecondaryScrapeResult;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.model.ScrapeObservation;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScrapeObservationService {
    private final ScrapeObservationRepository observationRepository;

    public ScrapeObservationService(ScrapeObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    public ScrapeObservation createObservation(Item item, LocalDateTime scrapedAt, SecondaryScrapeResult secondaryScrapeResult) {
        ScrapeObservation observation = new ScrapeObservation();
        observation.setItemId(item.getId());
        observation.setPrice(secondaryScrapeResult.getPrice());
        observation.setDiscountPercent(secondaryScrapeResult.getPercentage());
        observation.setAvailable(secondaryScrapeResult.isAvailable());
        observation.setScrapedAt(scrapedAt);
        return observationRepository.save(observation);
    }
}
