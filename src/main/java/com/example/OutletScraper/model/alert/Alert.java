package com.example.OutletScraper.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("alerts")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Alert {

    @Id
    private String id;

    private String itemId;

    private String message;

    private AlertType alertType;

    private LocalDateTime createdAt;

    private double oldPrice;
    private double newPrice;
}