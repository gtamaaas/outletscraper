package com.example.OutletScraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@Component
public class WebDriverFactory {

    public WebDriver create() {
        return new ChromeDriver();
        // or RemoteWebDriver, headless, etc.
    }
}