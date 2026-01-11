package org.example;

import org.example.input.FileParser;
import org.example.model.InputArticle;
import org.example.scraper.DriverFactory;
import org.example.scraper.OutletScraper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.sql.Driver;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String fileName = "list.txt";
        FileParser fileParser = new FileParser();
        List<InputArticle> list;
        try {
            list = fileParser.readFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DriverFactory driverFactory = new DriverFactory();
        WebDriver webDriver = driverFactory.create("firefox");
        OutletScraper outletScraper = new OutletScraper(list, webDriver);
        outletScraper.doScrape();
    }
}