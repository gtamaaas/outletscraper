package com.example.OutletScraper;

import com.example.OutletScraper.model.Alert.Alert;
import com.example.OutletScraper.model.Alert.AlertType;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.AlertRepository;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AlertServiceTest {


    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void cleanDb() {
        itemRepository.deleteAll();
        alertRepository.deleteAll();
    }


    @Test
    void shouldSendAlertWhenPriceIsLower() {
        Item item = new Item();
        item.setId("test");

        CurrentState currentState = new CurrentState();
        currentState.setPrice(200.0);

        item.setCurrentState(currentState);

        ScrapeObservation observation = mock(ScrapeObservation.class);
        when(observation.getPrice()).thenReturn(150.0);

        Alert alert = alertService.buildPriceDroppedAlert(item, currentState, observation);

        assertEquals("test", alert.getItemId());
        assertEquals(AlertType.PRICE_DROPPED, alert.getAlertType());
        assertEquals(
                "Price dropped from 200.0 to 150.0",
                alert.getMessage()
        );
    }

    @Test
    void shouldNotSendPriceDroppedAlertWhenPriceIsSame() {
        Item item = new Item();
        item.setId("test");

        CurrentState currentState = new CurrentState();
        currentState.setPrice(200.0);

        item.setCurrentState(currentState);

        ScrapeObservation observation = mock(ScrapeObservation.class);
        when(observation.getPrice()).thenReturn(200.0);

        alertService.evaluateAlerts(item, observation);

        Alert alert = alertRepository.findByItemId(item.getId());
        assertEquals(
                AlertType.LOWEST_PRICE_EVER,
                alert.getAlertType()
        );

    }

    @Test
    void shouldSendLowestPriceAlert() {
        Item item = new Item();
        item.setId("test");

        CurrentState currentState = new CurrentState();
        currentState.setPrice(200.0);

        item.setCurrentState(currentState);

        ScrapeObservation observation = mock(ScrapeObservation.class);

        when(observation.getPrice()).thenReturn(300.0);

        alertService.evaluateAlerts(item, observation);

        when(observation.getPrice()).thenReturn(100.0);

        alertService.evaluateAlerts(item, observation);

        List<Alert> alerts = alertRepository.findAllByItemId(item.getId());

        Alert alert = alerts.getLast();

        assertEquals(AlertType.LOWEST_PRICE_EVER, alert.getAlertType());
        assertEquals("New lowest price ever: " + 100.0, alert.getMessage());
    }
}
