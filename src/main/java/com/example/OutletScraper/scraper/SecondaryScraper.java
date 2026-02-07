package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.InternalCreateItemDto;
import com.example.OutletScraper.dto.scrapeResult.ScrapeResult;
import com.example.OutletScraper.dto.scrapeResult.SecondaryScrapeResult;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class SecondaryScraper extends AbstractScraper {

    @Override
    protected ScrapeResult buildResult(
            InternalCreateItemDto dto,
            WebDriver driver,
            double price,
            int discountPercent,
            boolean sizeAvailable
    ) {
        return new SecondaryScrapeResult(
                price,
                discountPercent,
                sizeAvailable,
                dto.getUrl(),
                dto.getSize()
        );
    }
}