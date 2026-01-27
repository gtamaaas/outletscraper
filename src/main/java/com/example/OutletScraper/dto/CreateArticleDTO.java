package com.example.OutletScraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateArticleDTO {
    private String name;
    private String size;
}
