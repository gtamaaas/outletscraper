package com.example.OutletScraper;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.Size;
import com.example.OutletScraper.repository.AlertRepository;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import com.example.OutletScraper.service.ItemService;
import com.example.OutletScraper.service.Scraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ScrapeObservationRepository scrapeObservationRepository;

    @MockitoBean
    private Scraper scraper;

    @Autowired
    private AlertRepository alertRepository;


    @BeforeEach
    void cleanDb() {
        itemRepository.deleteAll();
        scrapeObservationRepository.deleteAll();
        alertRepository.deleteAll();
    }


    @Test
    void newScrapeObservationShouldOverWriteItemState() {
        CreateItemDto createItemDto = new CreateItemDto("url here", Size.M);
        when(scraper.getPrice()).thenReturn(500.0);
        when(scraper.getPercentage()).thenReturn(0);
        when(scraper.isAvailable()).thenReturn(true);

        // inital, so that item gets created
        itemService.updateItem(createItemDto);

        when(scraper.getPrice()).thenReturn(450.0);
        when(scraper.getPercentage()).thenReturn(10);
        when(scraper.isAvailable()).thenReturn(true);

        // second, so that item gets updated
        itemService.updateItem(createItemDto);

        Item saved = itemRepository.findByUrl(createItemDto.getUrl()).orElseThrow();
        CurrentState currentState = saved.getCurrentState();
        assertEquals(450, currentState.getPrice());
        assertEquals(10, currentState.getDiscountPercent());
        assertTrue(currentState.isAvailable());

    }

}
