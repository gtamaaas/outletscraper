package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Article.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    Optional<Article> findByUrl(String url);
}
