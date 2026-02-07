package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.InternalCreateItemDto;
import com.example.OutletScraper.dto.ScrapeResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AbstractScraper {


    public ScrapeResult process(InternalCreateItemDto dto, WebDriver driver) {

        driver.get(dto.getUrl());

        double price = getPrice(driver);
        int discountPercent = getDiscountPercent(driver);
        boolean sizeAvailable = isSizeAvailable(driver, dto.getSize());

        return buildResult(dto, driver, price, discountPercent, sizeAvailable);
    }

    protected abstract ScrapeResult buildResult(
            InternalCreateItemDto dto,
            WebDriver driver,
            double price,
            int discountPercent,
            boolean sizeAvailable
    );

    // ===== Shared helpers =====

    protected boolean isSizeAvailable(WebDriver driver, String size) {
        WebElement element =
                driver.findElement(By.xpath("//span[text()='" + size + "']"));
        WebElement button =
                element.findElement(By.xpath("./ancestor::button"));
        return !button.getAttribute("id").contains("Unavailable");
    }

    protected double getPrice(WebDriver driver) {
        WebElement element =
                driver.findElement(By.xpath("//span[contains(@class,'finalPrice')]"));
        WebElement priceElement =
                element.findElement(By.xpath(".//ancestor::span/meta[@itemprop='price']"));
        return Double.parseDouble(priceElement.getAttribute("content"));
    }

    protected int getDiscountPercent(WebDriver driver) {
        WebElement element = driver.findElement(
                By.xpath("//div[contains(@class,'DiscountRate_discountRate')]//span")
        );
        String text = element.getText();
        return Integer.parseInt(text.replace("%", "").replace("-", "").trim());
    }
}