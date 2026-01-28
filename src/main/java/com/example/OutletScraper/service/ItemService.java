package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final ScrapeObservationRepository scrapeObservationRepository;
    private final Scraper scraper;

    public ItemService(ItemRepository itemRepository, ScrapeObservationRepository scrapeObservationRepository, Scraper scraper) {
        this.itemRepository = itemRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
        this.scraper = scraper;
    }

    public double findPreviousPrice(Item item) {
        String itemId = item.getId();
        Optional<ScrapeObservation> newestObservation =
                scrapeObservationRepository.findAllByItemId(itemId)
                        .stream()
                        .filter(elem -> elem.getScrapedAt() != null)
                        .max(Comparator.comparing(ScrapeObservation::getScrapedAt));

        return 1.0;
    }


    public void updateItem(CreateArticleDTO dto) {
        log.info("Scraping" + dto.toString());

        Item item = itemRepository
                .findByUrl(dto.getUrl())
                .orElseGet(() -> createInitialItem(dto));

        CurrentState currentState = secondaryUpdate(item);

        item.setCurrentState(currentState);
        item.setAvailable(scraper.isAvailable());
        LocalDateTime now = LocalDateTime.now();
        item.setFirstSeenAt(now);
        item.setLastSeenAt(now);
        itemRepository.save(item);

        log.info("Saved article {}", item);
    }


    private Item createInitialItem(CreateArticleDTO dto) {
        log.info("Creating new article for {}", dto.getUrl());

        Item item = new Item();
        item.setName(scraper.getName());
        item.setSize(dto.getSize());
        item.setUrl(dto.getUrl());
        item.setAvailable(scraper.isAvailable());


        return itemRepository.save(item);
    }




    @Transactional
    public CurrentState secondaryUpdate(Item item) {
        log.info("Performing secondary scrape for {}", item.getUrl());

        LocalDateTime now = LocalDateTime.now();

        // Observation (history)
        ScrapeObservation observation = new ScrapeObservation();
        observation.setPrice(scraper.getPrice());
        observation.setDiscountPercent(scraper.getPercentage());
        observation.setAvailability(scraper.isAvailable());
        observation.setScrapedAt(now);
        observation.setItemId(item.getId());

        scrapeObservationRepository.save(observation);

        // Update article timestamps
        item.setLastSeenAt(now);

        // Current state (latest snapshot)
        CurrentState currentState = new CurrentState();
        currentState.setPrice(scraper.getPrice());
        // TODO
        // findOlderpRice
        findPreviousPrice(item);
        currentState.setDiscountPercent(scraper.getPercentage());

        return currentState;
    }


}
