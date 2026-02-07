package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.scrapeResult.InitialScrapeResult;
import com.example.OutletScraper.dto.InternalCreateItemDto;
import com.example.OutletScraper.dto.scrapeResult.ScrapeResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class InitialScraper extends AbstractScraper {

    @Override
    protected ScrapeResult buildResult(
            InternalCreateItemDto dto,
            WebDriver driver,
            double price,
            int discountPercent,
            boolean sizeAvailable
    ) {

        String itemName =
                driver.findElement(By.tagName("h1")).getText();

        return new InitialScrapeResult(
                itemName,
                price,
                discountPercent,
                sizeAvailable,
                dto.getUrl(),
                dto.getSize()
        );
    }
}