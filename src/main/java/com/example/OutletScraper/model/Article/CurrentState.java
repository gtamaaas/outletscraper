package com.example.OutletScraper.model.Article;



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
    private double oldPrice;
    private int discountPercent;
}
