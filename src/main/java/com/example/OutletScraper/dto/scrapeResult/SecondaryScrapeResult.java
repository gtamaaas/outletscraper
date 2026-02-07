package com.example.OutletScraper.dto.scrapeResult;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecondaryScrapeResult extends ScrapeResult {

    public SecondaryScrapeResult(double price,
                                 int discountPercent,
                                 boolean sizeAvailable,
                                 String url,
                                 String size) {
        super(price, discountPercent, sizeAvailable, url, size);
    }


}
