package com.example.OutletScraper.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;

@Component
public class DriverFactory {
    public WebDriver create(String browser) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            case "chrome" -> new ChromeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }
}