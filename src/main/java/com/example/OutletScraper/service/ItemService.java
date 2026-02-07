package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.*;
import com.example.OutletScraper.dto.scrapeResult.InitialScrapeResult;
import com.example.OutletScraper.dto.scrapeResult.ScrapeResult;
import com.example.OutletScraper.dto.scrapeResult.SecondaryScrapeResult;
import com.example.OutletScraper.fileReaders.ItemImportService;
import com.example.OutletScraper.model.Analytics;
import com.example.OutletScraper.model.CurrentState;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.model.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.scraper.ScrapeService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private ScrapeService scrapeService;
    private ScrapeObservationService scrapeObservationService;
    private AlertService alertService;;

    public ItemService(ItemRepository itemRepository,
                       ScrapeService scrapeService,
                       ScrapeObservationService scrapeObservationService,
                       AlertService alertService) {
        this.itemRepository = itemRepository;
        this.scrapeService = scrapeService;
        this.scrapeObservationService = scrapeObservationService;
        this.alertService = alertService;
    }

    public Item upsertItem(CreateItemDto dto) {
        InternalCreateItemDto internal =
                new InternalCreateItemDto(dto.getUrl(), dto.getSize(),
                        itemRepository.existsByUrl(dto.getUrl()));

        ScrapeResult result =
                scrapeService.performSingleItemScrape(internal);

        if (result instanceof InitialScrapeResult initialResult) {
            return createNewItem(initialResult);
        }

        if (result instanceof SecondaryScrapeResult secondaryResult) {
            // existing item
            Item existingItem = itemRepository
                    .findByUrl(dto.getUrl())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Item marked as existing but not found: " + dto.getUrl()
                            )
                    );

            return updateExistingItem(existingItem, secondaryResult);
        }
        return null;
    }

    public List<Item> upsertItems(List<CreateItemDto> dtos) {

        List<InternalCreateItemDto> internalCreateItemDtos = new ArrayList<>();
        for (CreateItemDto dto : dtos) {
            internalCreateItemDtos.add(
                    new InternalCreateItemDto(
                            dto.getUrl(),
                            dto.getSize(),
                            itemRepository.existsByUrl(dto.getUrl())
                    )
            );
        }

        List<ScrapeResult> scrapeResultList =
                scrapeService.performItemsScrape(internalCreateItemDtos);

        List<Item> resultItems = new ArrayList<>();

        for (ScrapeResult scrapeResult : scrapeResultList) {

            if (scrapeResult instanceof InitialScrapeResult initialResult) {
                resultItems.add(createNewItem(initialResult));
                continue;
            }

            if (scrapeResult instanceof SecondaryScrapeResult secondaryResult) {
                Item existingItem = itemRepository
                        .findByUrl(scrapeResult.getUrl())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Item marked as existing but not found: " + scrapeResult.getUrl()
                                )
                        );

                resultItems.add(updateExistingItem(existingItem, secondaryResult));
            }
        }

        return resultItems;
    }


        public Item createNewItem(InitialScrapeResult scrapeResult) {
        Item item = new Item();
        item.setUrl(scrapeResult.getUrl());
        item.setName(scrapeResult.getName());
        item.setSize(scrapeResult.getSize());

        // originalPrice set only once
        CurrentState currentState = new CurrentState();
        currentState.setOriginalPrice(scrapeResult.getPrice());
        currentState.setCurrentPrice(scrapeResult.getPrice());
        currentState.setAvailable(scrapeResult.isAvailable());

        Analytics analytics = new Analytics();
        analytics.setLowestPriceEver(9999);

        item.setAnalytics(analytics);
        item.setCurrentState(currentState);

        LocalDateTime now = LocalDateTime.now();
        item.setFirstSeenAt(now);
        item.setLastSeenAt(now);


        itemRepository.save(item);
        return item;
    }



    public Item updateExistingItem(Item existingItem, SecondaryScrapeResult secondaryScrapeResult) {
        LocalDateTime now = LocalDateTime.now();

        ScrapeObservation observation =
                scrapeObservationService.createObservation(existingItem, now, secondaryScrapeResult);

        alertService.evaluateAlerts(existingItem, observation);

        updateItemCurrentState(existingItem, secondaryScrapeResult, now);

        itemRepository.save(existingItem);

        return existingItem;
    }

    public CurrentState updateItemCurrentState
            (Item item,
             SecondaryScrapeResult secondaryScrapeResult,
             LocalDateTime now) {

        CurrentState currentState = item.getCurrentState();
        currentState.setCurrentPrice(secondaryScrapeResult.getPrice());
        currentState.setDiscountPercent(secondaryScrapeResult.getPercentage());
        currentState.setAvailable(secondaryScrapeResult.isAvailable());

        item.setLastSeenAt(now);
        item.setCurrentState(currentState);

        return currentState;
    }

    public List<Item> getAllProducts() {
        return itemRepository.findAll();
    }


}
