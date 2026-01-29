package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
public class AnalyticsService {

    private final ItemRepository itemRepository;

    private final ScrapeObservationRepository scrapeObservationRepository;

    public AnalyticsService(ItemRepository itemRepository, ScrapeObservationRepository scrapeObservationRepository) {
        this.itemRepository = itemRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
    }

    public double calculateLowestPrice(Item item) {
        return scrapeObservationRepository.findAllByItemId(item.getId())
                .stream()
                .map(ScrapeObservation::getPrice)
                .min(Double::compareTo)
                .orElseThrow(() -> new IllegalStateException("No price observations found"));
    }


    public int daySinceObserved(Item item) {
        LocalDateTime firstTimeSeen = item.getFirstSeenAt();
        LocalDateTime today = LocalDateTime.now();

        return (int) ChronoUnit.DAYS.between(firstTimeSeen, today) + 1;
    }
}
