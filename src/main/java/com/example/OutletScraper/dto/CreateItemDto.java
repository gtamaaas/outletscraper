package com.example.OutletScraper.dto;

import com.example.OutletScraper.model.Item.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateItemDto {
    private String url;
    private Size size;
}
