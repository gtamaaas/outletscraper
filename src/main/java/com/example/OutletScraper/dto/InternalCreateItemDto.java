package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalCreateItemDto {
    private String url;
    private String size;
    private boolean available;
}
