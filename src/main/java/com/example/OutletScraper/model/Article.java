package com.example.OutletScraper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "articles")
@Data
@NoArgsConstructor
public class Article {
    @Id
    private String id;
    private String name;
    private String url;
    private Size size;
}
