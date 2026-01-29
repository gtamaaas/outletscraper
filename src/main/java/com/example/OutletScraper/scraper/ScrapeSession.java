package com.example.OutletScraper.scraper;

import org.openqa.selenium.WebDriver;

public class ScrapeSession implements AutoCloseable {

    private final WebDriver driver;

    public ScrapeSession(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver driver() {
        return driver;
    }

    @Override
    public void close() {
        driver.quit();
    }
}