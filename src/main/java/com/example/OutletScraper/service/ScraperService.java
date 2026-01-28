package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.model.Article.Article;
import com.example.OutletScraper.model.Article.CurrentState;
import com.example.OutletScraper.model.Article.ScrapeObservation;
import com.example.OutletScraper.repository.ArticleRepository;
import com.example.OutletScraper.repository.ScrapeObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScraperService {


    private final ArticleRepository articleRepository;
    private final ScrapeObservationRepository scrapeObservationRepository;

    public ScraperService(ArticleRepository articleRepository, ScrapeObservationRepository scrapeObservationRepository) {
        this.articleRepository = articleRepository;
        this.scrapeObservationRepository = scrapeObservationRepository;
    }

    public void initialScrape(CreateArticleDTO createArticleDTO) {
        Article article = new Article();
        article.setName(getName());
        article.setSize(createArticleDTO.getSize());


        article.setAvailable(isAvailable());

        CurrentState currentState = observeScrape(article);
        article.setCurrentState(currentState);

        articleRepository.save(article);
    }

    public CurrentState observeScrape(Article article) {
        ScrapeObservation scrapeObservation = new ScrapeObservation();
        scrapeObservation.setPrice(getPrice());
        scrapeObservation.setDiscountPercent(getPercentage());
        scrapeObservation.setAvailability(isAvailable());


        LocalDateTime currentTimeStamp = LocalDateTime.now();
        scrapeObservation.setScrapedAt(currentTimeStamp);
        if(article.getFirstSeenAt() == null)
            article.setFirstSeenAt(currentTimeStamp);
        article.setLastSeenAt(currentTimeStamp);


        CurrentState currentState = new CurrentState();
        currentState.setPrice(getPrice());
        // TODO
        // old price
        currentState.setDiscountPercent(getPercentage());

        scrapeObservationRepository.save(scrapeObservation);


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
