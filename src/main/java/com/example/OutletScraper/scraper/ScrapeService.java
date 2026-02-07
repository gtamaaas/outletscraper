package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ScrapeService {

    private final InitialScraper initialScraper;
    private final SecondaryScraper secondaryScraper;

    private WebDriver driver;

    public ScrapeService(InitialScraper initialScraper,
                         SecondaryScraper secondaryScraper) {
        this.initialScraper = initialScraper;
        this.secondaryScraper = secondaryScraper;
    }

    private void initializeDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
        }
    }

    private void destroyDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public ScrapeResult performSingleItemScrape(InternalCreateItemDto dto) {
        try {
            initializeDriver();

            AbstractScraper strategy =
                    dto.isAvailable() ? secondaryScraper : initialScraper;

            return strategy.process(dto, driver);

        } finally {
            destroyDriver();
        }
    }

    public List<ScrapeResult> performItemsScrape(List <InternalCreateItemDto> dtos) {
        try {
            initializeDriver();
            List <ScrapeResult> scrapeResultList = new ArrayList<>();
            dtos.forEach(dto -> {
                AbstractScraper strategy =
                        dto.isAvailable() ? secondaryScraper : initialScraper;
                ScrapeResult scrapeResult = strategy.process(dto, driver);
                scrapeResultList.add(scrapeResult);
            });
            return scrapeResultList;

        } finally {
            destroyDriver();
        }
    }
}
