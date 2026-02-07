package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Analytics;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.model.ScrapeObservation;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    public boolean checkForLowestPrice(Item item, ScrapeObservation scrapeObservation) {
        if (scrapeObservation.getPrice() < item.getAnalytics().getLowestPriceEver()) {
            item.getAnalytics().setLowestPriceEver(scrapeObservation.getPrice());
            return true;
        }
        return false;

    }
}
