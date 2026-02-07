package com.example.OutletScraper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "scrapeObservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapeObservation {

    @Id
    private String id;
    private String itemId;

    private double price;
    private int discountPercent;
    private String promotion;

    private boolean available;

    private LocalDateTime scrapedAt;
}
