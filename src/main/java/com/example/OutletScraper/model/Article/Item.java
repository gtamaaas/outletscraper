package com.example.OutletScraper.model.Article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "articles")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Item {
    @Id
    private String id;
    private String name;
    private String url;
    private Size size;
    private boolean isAvailable;

    private LocalDateTime firstSeenAt;
    private LocalDateTime lastSeenAt;


    private CurrentState currentState;
    private Analytics analytics;
}
