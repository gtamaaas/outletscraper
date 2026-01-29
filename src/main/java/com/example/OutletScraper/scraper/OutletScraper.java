package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.InitialScrapeResultDto;
import com.example.OutletScraper.dto.SecondaryScrapeResultDto;
import com.example.OutletScraper.model.Item.Size;
import com.example.OutletScraper.repository.ItemRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OutletScraper {
    private WebDriver webDriver;

    public OutletScraper( WebDriver webDriver, ItemRepository articleRepository) {
        this.webDriver = webDriver;
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    public String getArticleName() {
        return webDriver.findElement(By.tagName("h1")).getText();
    }

    public boolean isSizeAvailable(String size) {
        WebElement element  = webDriver.findElement(By.xpath("//span[text()='" + size +"']"));
        // ancestor reveals if the product is available
        WebElement button = element.findElement(By.xpath("./ancestor::button"));
        if(button.getAttribute("id").contains("Unavailable"))
            return false;
        else
            return true;
    }

    public double getPrice() {
        WebElement element = webDriver.findElement(By.xpath("//span[contains(@class, 'finalPrice')]" ));
        WebElement priceElement = element.findElement(By.xpath(".//ancestor::span/meta[@itemprop='price']"));
        return Double.parseDouble(priceElement.getAttribute("content"));
    }

    private int getDiscountPercent() {
        // todo
        return 0;
    }

    public SecondaryScrapeResultDto secondaryScrape(String url, Size size) {
        webDriver.get(url);
        double price = getPrice();
        int discountPercent = getDiscountPercent(); // implement if needed
        boolean available = isSizeAvailable(size.toString());
        return new SecondaryScrapeResultDto(price, discountPercent, available);
    }

    public InitialScrapeResultDto initialScrape(String url, Size size) {
        webDriver.get(url);
        String name = webDriver.findElement(By.tagName("h1")).getText();
        double price = getPrice();
        return new InitialScrapeResultDto(name, price);
    }


}