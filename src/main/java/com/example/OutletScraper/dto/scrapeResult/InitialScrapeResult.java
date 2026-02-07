package com.example.OutletScraper.dto.scrapeResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitialScrapeResult extends ScrapeResult {
    private String name;


    public InitialScrapeResult(
            String name,
            double price,
            int percentage,
            boolean available,
            String url,
            String size
    ) {
        super(price, percentage, available, url, size);
        this.name = name;
    }
}
