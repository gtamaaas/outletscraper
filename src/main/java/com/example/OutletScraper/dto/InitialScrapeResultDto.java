package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitialScrapeResultDto {
    private String name;
    private double price;
}
