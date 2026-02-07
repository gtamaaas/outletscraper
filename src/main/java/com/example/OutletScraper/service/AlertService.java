package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Alert;
import com.example.OutletScraper.model.AlertType;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.model.ScrapeObservation;
import com.example.OutletScraper.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public void evaluateAlerts(Item item, ScrapeObservation observation) {
        LocalDateTime now = observation.getScrapedAt();

        if (hasAvailabilityChanged(item, observation)) {
            createAvailabilityAlert(item, observation, now);
        }
        evaluatePriceAlerts(item, observation, now);
    }

    private boolean hasAvailabilityChanged(Item item, ScrapeObservation observation) {
        return item.getCurrentState().isAvailable() != observation.isAvailable();
    }

    private void createAvailabilityAlert(Item item, ScrapeObservation observation, LocalDateTime now) {
        Alert alert = new Alert();
        alert.setItemId(item.getId());
        alert.setCreatedAt(now);

        if (observation.isAvailable()) {
            alert.setAlertType(AlertType.BACK_IN_STOCK);
            alert.setNewPrice(observation.getPrice());
            alert.setMessage(String.format("Item '%s' is back in stock at %f",
                    item.getName(), observation.getPrice()));
        } else {
            alert.setAlertType(AlertType.NO_LONGER_AVAILABLE);
            alert.setOldPrice(item.getCurrentState().getCurrentPrice());
            alert.setMessage(String.format("Item '%s' is no longer available", item.getName()));
        }

        alertRepository.save(alert);
        printAlert(alert);
    }

    private void evaluatePriceAlerts(Item item, ScrapeObservation observation, LocalDateTime now) {
        double oldPrice = item.getCurrentState().getCurrentPrice();
        double newPrice = observation.getPrice();

        // Price drop or increase
        if (newPrice < oldPrice) {
            createPriceAlert(item, AlertType.PRICE_DROP, oldPrice, newPrice, now);
        } else if (newPrice > oldPrice) {
            createPriceAlert(item, AlertType.PRICE_WENT_UP, oldPrice, newPrice, now);
        }

        // Check if lowest price ever
        // TODO logic for this
//        if (isLowestPriceEver(item, newPrice)) {
//            createLowestPriceAlert(item, newPrice, now);
//        }
    }

    private void createPriceAlert(Item item, AlertType type, double oldPrice,
                                  double newPrice, LocalDateTime now) {
        Alert alert = new Alert();
        alert.setItemId(item.getId());
        alert.setAlertType(type);
        alert.setOldPrice(oldPrice);
        alert.setNewPrice(newPrice);
        alert.setCreatedAt(now);

        String direction = type == AlertType.PRICE_DROP ? "dropped" : "increased";
        alert.setMessage(String.format("Price %s for '%s': %s → %s",
                direction, item.getName(), oldPrice, newPrice));

        alertRepository.save(alert);
        printAlert(alert);
    }

    private void createLowestPriceAlert(Item item, double price, LocalDateTime now) {
        Alert alert = new Alert();
        alert.setItemId(item.getId());
        alert.setAlertType(AlertType.LOWEST_PRICE_EVER);
        alert.setNewPrice(price);
        alert.setCreatedAt(now);
        alert.setMessage(String.format("Lowest price ever for '%s': %s",
                item.getName(), price));

        alertRepository.save(alert);
        printAlert(alert);
    }

    private void printAlert(Alert alert) {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("ALERT: " + alert.getAlertType());
        System.out.println(alert.getMessage());
        System.out.println("Time: " + alert.getCreatedAt());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

}
