package com.example.OutletScraper.model.Item;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CurrentState {
    private double price;
    private double originalPrice;
    private int discountPercent;
}
