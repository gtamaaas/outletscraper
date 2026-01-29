package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        Item item = new Item();
        item.setName(scraper.getName());
        item.setSize(dto.getSize());
        item.setUrl(dto.getUrl());

        // originalPrice set only once
        CurrentState currentState = new CurrentState();
        currentState.setOriginalPrice(scraper.getPrice());

        item.setCurrentState(currentState);

        LocalDateTime now = LocalDateTime.now();
        item.setFirstSeenAt(now);
        item.setLastSeenAt(now);

        return itemRepository.save(item);
    }




    @Transactional
    public void secondaryUpdate(Item item) {
        log.info("Performing secondary scrape for {}", item.getUrl());

        LocalDateTime now = LocalDateTime.now();

        //ScrapeObservation save
        ScrapeObservation observation = new ScrapeObservation();
        observation.setPrice(scraper.getPrice());
        observation.setDiscountPercent(scraper.getPercentage());
        observation.setAvailability(scraper.isAvailable());
        observation.setScrapedAt(now);
        observation.setItemId(item.getId());
        scrapeObservationRepository.save(observation);

        item.setLastSeenAt(now);

        CurrentState currentState = item.getCurrentState();
        currentState.setPrice(scraper.getPrice());
        currentState.setDiscountPercent(scraper.getPercentage());
        currentState.setAvailable(scraper.isAvailable());
    }


}
