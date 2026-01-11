package org.example.scraper;

import org.example.model.InputArticle;
import org.example.model.OutPutArticle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OutletScraper {
    private List<InputArticle> listOfInputArticles;
    private WebDriver webDriver;

    public OutletScraper(List<InputArticle> listOfInputArticles, WebDriver webDriver) {
        this.listOfInputArticles = listOfInputArticles;
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

    public double getFinalPrice() {
        WebElement element = webDriver.findElement(By.xpath("//span[contains(@class, 'finalPrice')]" ));
        WebElement priceElement = element.findElement(By.xpath(".//ancestor::span/meta[@itemprop='price']"));
        return Double.parseDouble(priceElement.getAttribute("content"));
    }

    public List<OutPutArticle> doScrape() {
        List<OutPutArticle> outPutArticles = new ArrayList<>();
        for(InputArticle inputArticle : listOfInputArticles) {
            webDriver.get(inputArticle.getURL());
            String articleName = getArticleName();
            String size = inputArticle.getSize();
            Boolean isSizeAvailable = isSizeAvailable(size);
            double price = getFinalPrice();
            OutPutArticle outPutArticle = new OutPutArticle(articleName, isSizeAvailable, price, size);
            System.out.println("Created " + outPutArticle);
            outPutArticles.add(outPutArticle);

        }
        webDriver.quit();
        return outPutArticles;
    }
}
