package com.example.OutletScraper.model.Item;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Analytics {
    private double lowestPriceEver;
    private double avgPrice30d;
    private boolean isFakeDiscount;
    private int daysSinceObserved;

}
