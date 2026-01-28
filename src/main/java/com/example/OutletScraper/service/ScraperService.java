package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.model.Article.Article;
import com.example.OutletScraper.model.Article.CurrentState;
import com.example.OutletScraper.model.Article.ScrapeObservation;
import com.example.OutletScraper.repository.ArticleRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class ScraperService {


    private final ArticleRepository articleRepository;
    private final ScrapeObservationRepository scrapeObservationRepository;

    public ScraperService(ArticleRepository articleRepository, ScrapeObservationRepository scrapeObservationRepository) {
        this.articleRepository = articleRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
    }

    public void scrape(CreateArticleDTO dto) {
        log.info("Scraping {}", dto);

        Article article = articleRepository
                .findByUrl(dto.getUrl())
                .orElseGet(() -> createInitialArticle(dto));

        CurrentState currentState = secondaryScrape(article);

        article.setCurrentState(currentState);
        article.setAvailable(isAvailable());
        articleRepository.save(article);

        log.info("Saved article {}", article);
    }


    private Article createInitialArticle(CreateArticleDTO dto) {
        log.info("Creating new article for {}", dto.getUrl());

        Article article = new Article();
        article.setName(getName());
        article.setSize(dto.getSize());
        article.setUrl(dto.getUrl());
        article.setAvailable(isAvailable());

        LocalDateTime now = LocalDateTime.now();
        article.setFirstSeenAt(now);
        article.setLastSeenAt(now);

        return articleRepository.save(article);
    }



    @Transactional
    public CurrentState secondaryScrape(Article article) {
        log.info("Performing secondary scrape for {}", article.getUrl());

        LocalDateTime now = LocalDateTime.now();

        // Observation (history)
        ScrapeObservation observation = new ScrapeObservation();
        observation.setPrice(getPrice());
        observation.setDiscountPercent(getPercentage());
        observation.setAvailability(isAvailable());
        observation.setScrapedAt(now);
        observation.setArticleId(article.getId());

        scrapeObservationRepository.save(observation);

        // Update article timestamps
        article.setLastSeenAt(now);

        // Current state (latest snapshot)
        CurrentState currentState = new CurrentState();
        currentState.setPrice(getPrice());
        currentState.setDiscountPercent(getPercentage());

        return currentState;
    }

    public String getName() {
        // scraper logic IMPLEMENT
        return "pantofi";
    }

    public double getPrice() {
        //implement later
        return 100.0;
    }

    public int getPercentage() {
        //implement later
        return 10;
    }

    private boolean isAvailable() {
        //implement later
        return true;
    }

}
