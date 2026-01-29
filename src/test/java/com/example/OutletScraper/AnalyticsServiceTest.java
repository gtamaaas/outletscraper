package com.example.OutletScraper;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AnalyticsServiceTest {

    @MockitoBean
    private ScrapeObservationRepository scrapeObservationRepository;


    @Autowired
    private AnalyticsService analyticsService;


    @Test
    void shouldReturnLowestPrice() {

        Item item = new Item();
        item.setId("test");

        ScrapeObservation obs1 = mock(ScrapeObservation.class);
        ScrapeObservation obs2 = mock(ScrapeObservation.class);
        ScrapeObservation obs3 = mock(ScrapeObservation.class);

        when(obs1.getPrice()).thenReturn(500.0);
        when(obs2.getPrice()).thenReturn(5.0);
        when(obs3.getPrice()).thenReturn(25.99);

        when(scrapeObservationRepository.findAllByItemId(item.getId()))
                .thenReturn(List.of(obs1, obs2, obs3));


        double result = analyticsService.calculateLowestPrice(item);


        assertEquals(5.0, result);
    }

    @Test
    void ifObservedToDayValueShouldBeOne() {
        Item item = new Item();
        item.setFirstSeenAt(LocalDateTime.now());
        assertEquals(1, analyticsService.daySinceObserved(item));
    }
}
