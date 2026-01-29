package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Alert.Alert;
import com.example.OutletScraper.model.Alert.AlertType;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final AnalyticsService analyticsService;

    public AlertService(AlertRepository alertRepository,
                        AnalyticsService analyticsService) {
        this.alertRepository = alertRepository;
        this.analyticsService = analyticsService;
    }

    public void evaluateAlerts(Item item, ScrapeObservation newObs) {
        CurrentState current = item.getCurrentState();

        // Price dropped since last scrape
        if (newObs.getPrice() < current.getPrice()) {
            saveAndLog(buildPriceDroppedAlert(item, current, newObs));
        }

        // Lowest price ever
        if (analyticsService.lowestPrice(item, newObs)) {
            saveAndLog(buildLowestPriceAlert(item, newObs.getPrice()));
        }
    }

    private void saveAndLog(Alert alert) {
        alertRepository.save(alert);
        log.info(alert.getMessage());
    }

    public Alert buildPriceDroppedAlert(Item item,
                                        CurrentState current,
                                        ScrapeObservation newObs) {
        Alert alert = new Alert();
        alert.setItemId(item.getId());
        alert.setAlertType(AlertType.PRICE_DROPPED);
        alert.setMessage(
                "Price dropped from " + current.getPrice() +
                        " to " + newObs.getPrice()
        );
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }

    private Alert buildLowestPriceAlert(Item item, double price) {
        Alert alert = new Alert();
        alert.setItemId(item.getId());
        alert.setAlertType(AlertType.LOWEST_PRICE_EVER);
        alert.setMessage("New lowest price ever: " + price);
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }
}






