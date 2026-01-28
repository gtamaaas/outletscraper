package com.example.OutletScraper.model.Item;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// data regarding a scrape
@Document(collection = "scrapeObservations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScrapeObservation {

    @Id
    private String id;
    private String itemId;

    private double price;
    private int discountPercent;
    private String promotion;

    private boolean availability;

    private LocalDateTime scrapedAt;
}
