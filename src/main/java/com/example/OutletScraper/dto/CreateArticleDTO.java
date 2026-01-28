package com.example.OutletScraper.dto;

import com.example.OutletScraper.model.Article.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateArticleDTO {
    private String name;
    private Size size;
}
