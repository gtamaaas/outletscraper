package org.example.scraper;

import org.example.model.InputArticle;
import org.example.model.OutPutArticle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class OutletScraper {
    private List<InputArticle> listOfInputArticles;
    private WebDriver webDriver;

    public OutletScraper(List<InputArticle> listOfInputArticles, WebDriver webDriver) {
        this.listOfInputArticles = listOfInputArticles;
        this.webDriver = webDriver;
    }

    public List<OutPutArticle> doScrape() {
        webDriver.quit();
        return null;
    }
}
