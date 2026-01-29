package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Item.Analytics;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.ItemRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class AnalyticsService {

    private static final double EPSILON = 0.0001;

    private final ItemRepository itemRepository;

    private final ScrapeObservationRepository scrapeObservationRepository;

    public AnalyticsService(ItemRepository itemRepository, ScrapeObservationRepository scrapeObservationRepository) {
        this.itemRepository = itemRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
    }

    public void updateAnalytics(Item item) {
        Analytics analytics = new Analytics();
        int daySinceObserved = daySinceObserved(item);
        boolean isFakeDiscount = isFakeDiscount(item);

        analytics.setDaysSinceObserved(daySinceObserved);
        analytics.setFakeDiscount(isFakeDiscount);

        item.setAnalytics(analytics);

        itemRepository.save(item);

    }

    public boolean lowestPrice(Item item, ScrapeObservation observation) {
        Analytics analytics = item.getAnalytics();

        if (analytics == null) {
            analytics = new Analytics();
            item.setAnalytics(analytics);
        }

        if (analytics.getLowestPriceEver() == 0 ||
                observation.getPrice() < analytics.getLowestPriceEver()) {

            analytics.setLowestPriceEver(observation.getPrice());
            return true;
        }

        return false;
    }


    public int daySinceObserved(Item item) {
        LocalDateTime firstTimeSeen = item.getFirstSeenAt();
        LocalDateTime today = LocalDateTime.now();

        return (int) ChronoUnit.DAYS.between(firstTimeSeen, today) + 1;
    }

    public boolean isFakeDiscount(Item item) {

        double currentPrice = item.getCurrentState().getPrice();
        double originalPrice = item.getCurrentState().getOriginalPrice();
        int discount = item.getCurrentState().getDiscountPercent();

        double expectedPrice =
                originalPrice * (1 - discount / 100.0);

        log.info("{} == {}", currentPrice, expectedPrice);

        return Math.abs(currentPrice - expectedPrice) > EPSILON;
    }


}
