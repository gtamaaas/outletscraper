package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeResult{
    private double price;
    private int percentage;
    private boolean available;
    private String url;
    public String size;
}
