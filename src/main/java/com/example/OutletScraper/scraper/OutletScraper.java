package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.InitialScrapeResultDto;
import com.example.OutletScraper.dto.SecondaryScrapeResultDto;
import com.example.OutletScraper.model.Item.Size;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OutletScraper {

    public String getArticleName(WebDriver webDriver) {
        return webDriver.findElement(By.tagName("h1")).getText();
    }

    public boolean isSizeAvailable(String size, WebDriver webDriver) {
        WebElement element  = webDriver.findElement(By.xpath("//span[text()='" + size +"']"));
        // ancestor reveals if the product is available
        WebElement button = element.findElement(By.xpath("./ancestor::button"));
        if(button.getAttribute("id").contains("Unavailable"))
            return false;
        else
            return true;
    }

    public double getPrice(WebDriver webDriver) {
        WebElement element = webDriver.findElement(By.xpath("//span[contains(@class, 'finalPrice')]" ));
        WebElement priceElement = element.findElement(By.xpath(".//ancestor::span/meta[@itemprop='price']"));
        return Double.parseDouble(priceElement.getAttribute("content"));
    }

    private int getDiscountPercent(WebDriver webDriver) {
        // todo
        return 0;
    }

    public SecondaryScrapeResultDto secondaryScrape(String url, Size size, WebDriver webDriver) {
        webDriver.get(url);
        double price = getPrice(webDriver);
        int discountPercent = getDiscountPercent(webDriver); // implement if needed
        boolean available = isSizeAvailable(size.toString(), webDriver);
        return new SecondaryScrapeResultDto(price, discountPercent, available);
    }

    public InitialScrapeResultDto initialScrape(String url, Size size, WebDriver webDriver) {
        webDriver.get(url);
        String name = webDriver.findElement(By.tagName("h1")).getText();
        double price = getPrice(webDriver);
        return new InitialScrapeResultDto(name, price);
    }


}