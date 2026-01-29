package com.example.OutletScraper.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ScrapeSessionFactory {

    public WebDriver open() {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        return driver;
    }
}