package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.model.Article.Item;
import com.example.OutletScraper.model.Article.CurrentState;
import com.example.OutletScraper.model.Article.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ScraperService {


    private final ItemRepository itemRepository;
    private final ScrapeObservationRepository scrapeObservationRepository;

    public ScraperService(ItemRepository itemRepository, ScrapeObservationRepository scrapeObservationRepository) {
        this.itemRepository = itemRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
    }

    public void scrape(CreateArticleDTO dto) {
        log.info("Scraping" + dto.toString());

        Item item = itemRepository
                .findByUrl(dto.getUrl())
                .orElseGet(() -> createInitialArticle(dto));

        CurrentState currentState = secondaryScrape(item);

        item.setCurrentState(currentState);
        item.setAvailable(isAvailable());
        LocalDateTime now = LocalDateTime.now();
        item.setFirstSeenAt(now);
        item.setLastSeenAt(now);
        itemRepository.save(item);

        log.info("Saved article {}", item);
    }


    private Item createInitialArticle(CreateArticleDTO dto) {
        log.info("Creating new article for {}", dto.getUrl());

        Item item = new Item();
        item.setName(getName());
        item.setSize(dto.getSize());
        item.setUrl(dto.getUrl());
        item.setAvailable(isAvailable());




        return itemRepository.save(item);
    }



    @Transactional
    public CurrentState secondaryScrape(Item item) {
        log.info("Performing secondary scrape for {}", item.getUrl());

        LocalDateTime now = LocalDateTime.now();

        // Observation (history)
        ScrapeObservation observation = new ScrapeObservation();
        observation.setPrice(getPrice());
        observation.setDiscountPercent(getPercentage());
        observation.setAvailability(isAvailable());
        observation.setScrapedAt(now);
        observation.setItemId(item.getId());

        scrapeObservationRepository.save(observation);

        // Update article timestamps
        item.setLastSeenAt(now);

        // Current state (latest snapshot)
        CurrentState currentState = new CurrentState();
        currentState.setPrice(getPrice());
        currentState.setDiscountPercent(getPercentage());

        return currentState;
    }

    public String getName() {
        // scraper logic IMPLEMENT
        return "pantofi";
    }

    public double getPrice() {
        //implement later
        return 100.0;
    }

    public int getPercentage() {
        //implement later
        return 10;
    }

    private boolean isAvailable() {
        //implement later
        return true;
    }

}
