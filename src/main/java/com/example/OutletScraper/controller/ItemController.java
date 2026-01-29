package com.example.OutletScraper.controller;


import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.service.FileParser;
import com.example.OutletScraper.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@Slf4j
public class ItemController {

    private final FileParser fileParser;
    private final ItemService itemService;
    private final String inputFile;

    public ItemController(
            FileParser fileParser,
            ItemService itemService,
            @Value("${scraper.input-file}") String inputFile) {

        this.fileParser = fileParser;
        this.itemService =  itemService;
        this.inputFile = inputFile;
    }


    @PostMapping("/read-from-file")
    public ResponseEntity<String> scrapeFromFile() {
        log.info("Starting scrape from file: {}");

        List<CreateItemDto> items = fileParser.readFile(inputFile);

        for (CreateItemDto dto : items) {
            itemService.updateItem(dto);
        }


        return ResponseEntity.ok(
                "Processed " + items.size() + " items from file"
        );
    }

}
