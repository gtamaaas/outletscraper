package com.example.OutletScraper;

import com.example.OutletScraper.model.Item.Analytics;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import com.example.OutletScraper.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AnalyticsServiceTest {

    @MockitoBean
    private ScrapeObservationRepository scrapeObservationRepository;


    @Autowired
    private AnalyticsService analyticsService;

    @Test
    void ifObservedToDayValueShouldBeOne() {
        Item item = new Item();
        item.setFirstSeenAt(LocalDateTime.now());
        assertEquals(1, analyticsService.daySinceObserved(item));
    }


    @Test
    void ifPriceIsBiggerThanOriginalPriceThenFakeDiscount() {
        Item item = new Item();
        CurrentState currentState = new CurrentState();
        item.setCurrentState(currentState);
        item.getCurrentState().setOriginalPrice(500);
        // with these percentage, the real price should be 571.42
        item.getCurrentState().setPrice(400);
        item.getCurrentState().setDiscountPercent(30);

        assertTrue(analyticsService.isFakeDiscount(item));

    }


    @Test
    void shouldReturnTrueForRealDiscounts() {
        Item item = new Item();
        CurrentState currentState = new CurrentState();
        item.setCurrentState(currentState);
        item.getCurrentState().setOriginalPrice(577);
        item.getCurrentState().setPrice(375.05);
        item.getCurrentState().setDiscountPercent(35);

        assertFalse(analyticsService.isFakeDiscount(item));

    }

    @Test
    void analyticsShouldBeUpdated() {
        Item item = new Item();
        item.setId("test");
        item.setFirstSeenAt(LocalDateTime.now().minusDays(2));

        CurrentState state = new CurrentState();
        state.setOriginalPrice(100);
        state.setPrice(80);
        state.setDiscountPercent(20);
        item.setCurrentState(state);

        ScrapeObservation obs1 = mock(ScrapeObservation.class);
        ScrapeObservation obs2 = mock(ScrapeObservation.class);

        when(obs1.getPrice()).thenReturn(90.0);
        when(obs2.getPrice()).thenReturn(80.0);

        when(scrapeObservationRepository.findAllByItemId("test"))
                .thenReturn(List.of(obs1, obs2));

        analyticsService.updateAnalytics(item);

        assertNotNull(item.getAnalytics());

        Analytics analytics = item.getAnalytics();
        assertEquals(3, analytics.getDaysSinceObserved()); // inclusive
        assertFalse(analytics.isFakeDiscount());

    }

}
