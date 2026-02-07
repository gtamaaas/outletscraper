package com.example.OutletScraper.model;


import lombok.Data;

@Data
public class CurrentState {
    private double currentPrice;
    private double originalPrice;
    private int discountPercent;
    private boolean available;
}
