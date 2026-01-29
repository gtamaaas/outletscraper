package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.dto.InitialScrapeResultDto;
import com.example.OutletScraper.dto.SecondaryScrapeResultDto;
import com.example.OutletScraper.model.Alert.Alert;
import com.example.OutletScraper.model.Item.Analytics;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import com.example.OutletScraper.scraper.OutletScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final ScrapeObservationRepository scrapeObservationRepository;
    private final OutletScraper scraper;
    private final AnalyticsService analyticsService;
    private final AlertService alertService;

    public ItemService(ItemRepository itemRepository,
                       ScrapeObservationRepository scrapeObservationRepository,
                       OutletScraper scraper, AnalyticsService analyticsService,
                       AlertService alertService) {
        this.itemRepository = itemRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
        this.scraper = scraper;
        this.analyticsService = analyticsService;
        this.alertService = alertService;
    }

    public void updateItem(CreateItemDto dto) {
        log.info("Scraping {}", dto);

        Item item = itemRepository
                .findByUrl(dto.getUrl())
                .orElseGet(() -> createInitialItem(dto));

        secondaryUpdate(item);

        itemRepository.save(item);

        log.info("Saved article {}", item);
    }


    private Item createInitialItem(CreateItemDto dto) {
        log.info("Creating new article for {}", dto.getUrl());

        InitialScrapeResultDto initialScrapeResultDto = scraper.initialScrape(dto.getUrl(), dto.getSize());

        Item item = new Item();
        item.setName(initialScrapeResultDto.getName());
        item.setSize(dto.getSize());
        item.setUrl(dto.getUrl());

        // originalPrice set only once
        CurrentState currentState = new CurrentState();
        currentState.setOriginalPrice(initialScrapeResultDto.getPrice());

        item.setCurrentState(currentState);

        LocalDateTime now = LocalDateTime.now();
        item.setFirstSeenAt(now);
        item.setLastSeenAt(now);

        return itemRepository.save(item);
    }


    @Transactional
    public void secondaryUpdate(Item item) {
        log.info("Performing secondary scrape for {}", item.getUrl());
        SecondaryScrapeResultDto secondaryScrapeResultDto = scraper.secondaryScrape(item.getUrl(), item.getSize());
        LocalDateTime now = LocalDateTime.now();

        ScrapeObservation observation = new ScrapeObservation();
        observation.setPrice(secondaryScrapeResultDto.getPrice());
        observation.setDiscountPercent(secondaryScrapeResultDto.getPercentage());
        observation.setAvailability(secondaryScrapeResultDto.isAvailable());
        observation.setScrapedAt(now);
        observation.setItemId(item.getId());
        scrapeObservationRepository.save(observation);

        // Evaluate alerts before mutating state
        alertService.evaluateAlerts(item, observation);

        itemRepository.save(item);

        // Update item state
        item.setLastSeenAt(now);
        CurrentState currentState = item.getCurrentState();
        currentState.setPrice(observation.getPrice());
        currentState.setDiscountPercent(observation.getDiscountPercent());
        currentState.setAvailable(observation.isAvailability());
    }


}
