package com.example.OutletScraper.config;


import com.example.OutletScraper.scraper.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

    @Bean(destroyMethod = "quit")
    public WebDriver webDriver(DriverFactory driverFactory,
                               @Value("${scraper.browser:chrome}") String browser) {
        return driverFactory.create(browser);
    }
}