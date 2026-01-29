package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecondaryScrapeResultDto {
    private double price;
    private int percentage;
    private boolean isAvailable;
}

