package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateDto {
    private double newerPrice;
    private int percentage;
    private boolean available;
}
