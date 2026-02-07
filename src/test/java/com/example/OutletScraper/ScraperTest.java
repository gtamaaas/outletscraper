package com.example.OutletScraper;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.dto.InternalCreateItemDto;
import com.example.OutletScraper.dto.ScrapeResult;
import com.example.OutletScraper.scraper.ScrapeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class ScraperTest {

    @Autowired
    private ScrapeService scrapeService;

    final String itemToTest1 = "src/test/resources/testSites/itemToTest1.mhtml";

    @Test
    public void scraperObjectGetsCreated() {


        Scraper scraper = Mockito.mock(Scraper.class);

        when(scraper.getItemName()).thenReturn("Sneakers");
        when(scraper.isSizeAvailable("38")).thenReturn(true);
        when(scraper.getPrice()).thenReturn(100.0);
        when(scraper.getDiscountPercent()).thenReturn(20);
    }

    @Test
    public void scraperFindsCorrectDiscountPercentage() {
        Path htmlPath = Path.of(itemToTest1);
        String fileUrl = htmlPath.toAbsolutePath().toUri().toString();

        InternalCreateItemDto createItemDto = new InternalCreateItemDto(fileUrl, "M", true);
        ScrapeResult scrapeResult = scrapeService.performSingleItemScrape(createItemDto);
        assertEquals(scrapeResult.getPercentage(), 29);
    }
}
