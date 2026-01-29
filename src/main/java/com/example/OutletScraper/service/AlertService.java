package com.example.OutletScraper.service;

import com.example.OutletScraper.model.Alert.Alert;
import com.example.OutletScraper.model.Alert.AlertType;
import com.example.OutletScraper.model.Item.CurrentState;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.ScrapeObservation;
import com.example.OutletScraper.repository.AlertRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert buildPriceDroppedAlert(Item item, ScrapeObservation newObs) {
        CurrentState current = item.getCurrentState();

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

    public void evaluateAlerts(Item item, ScrapeObservation newObs) {
        CurrentState currentState = item.getCurrentState();
        if (newObs.getPrice() < currentState.getPrice()) {
            Alert alert = buildPriceDroppedAlert(item, newObs);
            alertRepository.save(alert);
            log.info(alert.getMessage());
        }
    }




}
