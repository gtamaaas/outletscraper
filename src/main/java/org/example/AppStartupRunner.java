package org.example;

import org.example.input.FileParser;
import org.example.model.InputArticle;
import org.example.scraper.DriverFactory;
import org.example.scraper.OutletScraper;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final FileParser fileParser;
    private final OutletScraper outletScraper;
    private final String inputFile;

    public AppStartupRunner(
            FileParser fileParser,
            OutletScraper outletScraper,
            @Value("${scraper.input-file}") String inputFile) {

        this.fileParser = fileParser;
        this.outletScraper = outletScraper;
        this.inputFile = inputFile;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<InputArticle> articles = fileParser.readFile(inputFile);
        outletScraper.doScrape(articles);
    }
}