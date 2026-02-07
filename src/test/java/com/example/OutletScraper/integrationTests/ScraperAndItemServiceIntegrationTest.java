package com.example.OutletScraper.integrationTests;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.model.Analytics;
import com.example.OutletScraper.model.CurrentState;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.repository.AlertRepository;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.scraper.ScrapeService;
import com.example.OutletScraper.service.AlertService;
import com.example.OutletScraper.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class ScraperAndItemServiceIntegrationTest {
    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ScrapeService scrapeService;

    @Autowired
    private ItemService itemService;

    final String nonExistingPage = "src/test/resources/testSites/nonExistingPage.mhtml";
    final String itemStillAvailable = "src/test/resources/testSites/itemStillAvailable.mhtml";
    final String itemToTest1 = "src/test/resources/testSites/itemToTest1.mhtml";
    final String itemToTest2 = "src/test/resources/testSites/itemToTest2.mhtml";

    @BeforeEach
    void cleanUp() {
        itemRepository.deleteAll();
        alertRepository.deleteAll();
    }



    @Test
    public void shouldScrapeAndSaveNonExistingItem() {
        Path htmlPath = Path.of(itemStillAvailable);
        String fileUrl = htmlPath.toAbsolutePath().toUri().toString();


        CreateItemDto createItemDto = new CreateItemDto(fileUrl, "38");
        Item item = itemService.upsertItem(createItemDto);
        assertEquals("PANTALONI SLIM FIT COMFORT STRETCH CU ȘNUR", item.getName());
        assertTrue(item.getCurrentState().isAvailable());
        assertEquals(82.99, item.getCurrentState().getCurrentPrice());

    }

    @Test
    public void shouldScrapeAndUpdateAlreadyExistingItem() {
        Path htmlPath = Path.of(itemStillAvailable);
        String fileUrl = htmlPath.toAbsolutePath().toUri().toString();

        // MANUALLY setting up db with an item
        // so that it is available from the start
        Item item = new Item();
        item.setUrl(fileUrl);
        item.setName("PANTALONI SLIM FIT COMFORT STRETCH CU ȘNUR");
        item.setSize("42");

        CurrentState currentState = new CurrentState();
        currentState.setCurrentPrice(500.0);
        item.setCurrentState(currentState);

        Analytics analytics = new Analytics();
        analytics.setLowestPriceEver(50);
        item.setAnalytics(analytics);

        itemRepository.save(item);

        // creating new Dto request
        CreateItemDto createItemDto = new CreateItemDto(fileUrl, "42");
        Item newItem = itemService.upsertItem(createItemDto);

        //
        assertFalse(newItem.getCurrentState().isAvailable());

    }

    @Test
    public void batchUpdateShouldWork() {
        Path htmlPath = Path.of(itemStillAvailable);
        String fileUrl = htmlPath.toAbsolutePath().toUri().toString();

        CurrentState currentState = new CurrentState();

        Item item = new Item();
        item.setUrl(fileUrl);
        item.setName("PANTALONI SLIM FIT COMFORT STRETCH CU ȘNUR");
        item.setCurrentState(currentState);

        Analytics analytics = new Analytics();
        analytics.setLowestPriceEver(50);
        item.setAnalytics(analytics);

        itemRepository.save(item);

        CreateItemDto createItemDto1 = new CreateItemDto(fileUrl, "42");
        CreateItemDto createItemDto3 = new CreateItemDto(fileUrl, "38");

        htmlPath = Path.of(itemToTest1);
        fileUrl = htmlPath.toAbsolutePath().toUri().toString();

        CreateItemDto createItemDto2 = new CreateItemDto(fileUrl, "M");



        List<CreateItemDto> createItemDtoList = new ArrayList<>();
        createItemDtoList.add(createItemDto1);
        createItemDtoList.add(createItemDto2);
        createItemDtoList.add(createItemDto3);


        itemService.upsertItems(createItemDtoList);
        Optional<Item> newlyAddedItem = itemRepository.findByUrl(fileUrl);


        assertEquals(newlyAddedItem.get().getName(), "Cămașă slim fit din bumbac 100%".toUpperCase());
    }

    @Test
    public void multipleCreatesShouldWork() {
        Path htmlPath = Path.of(itemStillAvailable);
        String fileUrl1 = htmlPath.toAbsolutePath().toUri().toString();

        htmlPath = Path.of(itemToTest1);
        String fileUrl2 = htmlPath.toAbsolutePath().toUri().toString();

        htmlPath = Path.of(itemToTest2);
        String fileUrl3 = htmlPath.toAbsolutePath().toUri().toString();

        CreateItemDto createItemDto1 = new CreateItemDto(fileUrl1, "38");
        CreateItemDto createItemDto2 = new CreateItemDto(fileUrl2, "M");
        CreateItemDto createItemDto3 = new CreateItemDto(fileUrl3, "M");

        List<CreateItemDto> list = new ArrayList<>();
        list.add(createItemDto1);
        list.add(createItemDto2);
        list.add(createItemDto3);

        itemService.upsertItems(list);
    }
    @Test
    void addingTwoItemsOneAfterAnotherShouldntCreateTwoItems() {
        Path htmlPath = Path.of(itemStillAvailable);
        String fileUrl1 = htmlPath.toAbsolutePath().toUri().toString();

        CreateItemDto createItemDto1 = new CreateItemDto(fileUrl1, "38");
        CreateItemDto createItemDto2 = new CreateItemDto(fileUrl1, "38");

        itemService.upsertItem(createItemDto1);
        itemService.upsertItem(createItemDto2);


        assertEquals(1, itemRepository.count());
    }
}


