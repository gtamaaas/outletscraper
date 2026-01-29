package com.example.OutletScraper;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.scraper.OutletScraper;
import com.example.OutletScraper.service.FileParser;
import com.example.OutletScraper.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final FileParser fileParser;
    private final ItemService itemService;
    private final String inputFile;

    public AppStartupRunner(
            FileParser fileParser,
            ItemService itemService,
            @Value("${scraper.input-file}") String inputFile) {

        this.fileParser = fileParser;
        this.itemService =  itemService;
        this.inputFile = inputFile;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<CreateItemDto> items = fileParser.readFile(inputFile);
       items.forEach(item -> itemService.updateItem(item));
    }
}