package com.example.OutletScraper.integrationTests;


import com.example.OutletScraper.dto.scrapeResult.SecondaryScrapeResult;
import com.example.OutletScraper.model.Analytics;
import com.example.OutletScraper.model.CurrentState;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.model.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import com.example.OutletScraper.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceIntegrationTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ScrapeObservationRepository scrapeObservationRepository;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void cleanUp() {
        itemRepository.deleteAll();
        scrapeObservationRepository.deleteAll();
    }

    @Test
    void updateExistingItemShouldCreateObservationThatMatchesItem() {

        Item existingItem = new Item();
        existingItem.setId("testid");
        itemRepository.save(existingItem);
        CurrentState currentState = new CurrentState();
        currentState.setCurrentPrice(50.0);
        currentState.setDiscountPercent(10);
        currentState.setAvailable(false);
        existingItem.setCurrentState(currentState);
        Analytics analytics = new Analytics();
        analytics.setLowestPriceEver(9999);
        existingItem.setAnalytics(analytics);

        SecondaryScrapeResult scrapeResult = new SecondaryScrapeResult();
        scrapeResult.setPrice(40.0);
        scrapeResult.setPercentage(20);
        scrapeResult.setAvailable(true);

        itemService.updateExistingItem(existingItem, scrapeResult);

        Optional<ScrapeObservation> observations =
                scrapeObservationRepository.findByItemId(existingItem.getId());
        System.out.println(observations.get().toString());

        ScrapeObservation observation = observations.get();
        assertEquals(existingItem.getId(), observation.getItemId());
        assertEquals(40.0, observation.getPrice());
        assertEquals(20, observation.getDiscountPercent());
        assertTrue(observation.isAvailable());

    }

    @Test
    void updateExistingItemShouldUpdateItemStateOnly() {
        // given
        Item existingItem = new Item();
        existingItem.setId("testid");
        CurrentState currentState = new CurrentState();
        currentState.setCurrentPrice(500.0);
        existingItem.setCurrentState(currentState);

        Analytics analytics = new Analytics();
        analytics.setLowestPriceEver(9999);
        existingItem.setAnalytics(analytics);

        SecondaryScrapeResult scrapeResult = new SecondaryScrapeResult();
        scrapeResult.setPrice(40.0);
        scrapeResult.setPercentage(20);
        scrapeResult.setAvailable(true);

        // when
        itemService.updateExistingItem(existingItem, scrapeResult);

        // then
        assertEquals(40.0, existingItem.getCurrentState().getCurrentPrice());
        assertEquals(20, existingItem.getCurrentState().getDiscountPercent());
        assertTrue(existingItem.getCurrentState().isAvailable());
    }
}
