package org.example;

import org.example.input.FileParser;
import org.example.model.InputArticle;
import org.example.scraper.DriverFactory;
import org.example.scraper.OutletScraper;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String fileName = "list.txt";
        FileParser fileParser = new FileParser();
        List<InputArticle> list;
        try {
            list = fileParser.readFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DriverFactory driverFactory = new DriverFactory();
        WebDriver webDriver = driverFactory.create("chrome");
        OutletScraper outletScraper = new OutletScraper(list, webDriver);
        outletScraper.doScrape();
    }
}