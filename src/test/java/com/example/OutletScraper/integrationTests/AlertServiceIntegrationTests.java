package com.example.OutletScraper.integrationTests;

import com.example.OutletScraper.model.*;
import com.example.OutletScraper.model.alert.Alert;
import com.example.OutletScraper.model.alert.AlertType;
import com.example.OutletScraper.repository.AlertRepository;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class AlertServiceIntegrationTests {

    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void cleanUp() {
        itemRepository.deleteAll();
        alertRepository.deleteAll();
    }

    @Test
    void whenItemBecomesAvailableShouldSaveBackInStockAlert() {
        // Given - save real entities to database
        Item item = new Item();
        item.setName("Test Product");
        CurrentState state = new CurrentState();
        state.setAvailable(false);
        state.setCurrentPrice(100.0);
        item.setCurrentState(state);
        item = itemRepository.save(item);

        ScrapeObservation observation = new ScrapeObservation();
        observation.setScrapedAt(LocalDateTime.now());
        observation.setAvailable(true);
        observation.setPrice(100.0);

        // When
        alertService.evaluateAlerts(item, observation);

        // Then - verify alert was actually saved to database
        List<Alert> alerts = alertRepository.findByItemId(item.getId());
        assertEquals(1, alerts.size());

        Alert savedAlert = alerts.get(0);
        assertEquals(AlertType.BACK_IN_STOCK, savedAlert.getAlertType());
        assertEquals(100.0, savedAlert.getNewPrice());
        assertNotNull(savedAlert.getId()); // Has DB-generated ID
    }

    @Test
    void whenPriceDropsAndItemBecomesAvailableShouldSaveBothAlerts() {
        //given
        Item item = new Item();
        item.setName("Test Product");
        CurrentState state = new CurrentState();
        state.setAvailable(false);
        state.setCurrentPrice(100.0);
        item.setCurrentState(state);
        item = itemRepository.save(item);

        ScrapeObservation observation = new ScrapeObservation();
        observation.setScrapedAt(LocalDateTime.now());
        observation.setAvailable(true);
        observation.setPrice(80.0);

        // When
        alertService.evaluateAlerts(item, observation);

        // Then
        List<Alert> alerts = alertRepository.findByItemId(item.getId());
        assertEquals(2, alerts.size());

        assertTrue(alerts.stream()
                .anyMatch(a -> a.getAlertType() == AlertType.BACK_IN_STOCK));
        assertTrue(alerts.stream()
                .anyMatch(a -> a.getAlertType() == AlertType.PRICE_DROP));
    }

    @Test
    void whenPriceRemainSameAndObservationRemainsSameNoAlertShouldBeCreated() {
        Item item = new Item();
        item.setName("Test Product");
        CurrentState state = new CurrentState();
        state.setAvailable(true);
        state.setCurrentPrice(100.0);
        item.setCurrentState(state);
        item = itemRepository.save(item);

        ScrapeObservation observation = new ScrapeObservation();
        observation.setScrapedAt(LocalDateTime.now());
        observation.setAvailable(true);
        observation.setPrice(100.0);

        // Then
        List<Alert> alerts = alertRepository.findByItemId(item.getId());
        assertEquals(0, alerts.size());
    }
}
