package com.example.OutletScraper.controller;


import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.fileReaders.ItemImportService;
import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemImportService itemImportService;


    public ItemController(ItemService itemService, ItemImportService itemImportService) {
        this.itemService = itemService;
        this.itemImportService = itemImportService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllProducts() {
        List<Item> products = itemService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @PostMapping("/batchScrape")
    public ResponseEntity<List<Item>> scrapeFromFile() {
        try {
            List<CreateItemDto> items = itemImportService.loadItemsFromFile("src/main/resources/data/list.txt");
            List<Item> createdOrUpdatedItems = itemService.upsertItems(items);
            return ResponseEntity.ok(createdOrUpdatedItems);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<Item> scrapeIndividualItem(
            @RequestBody
            CreateItemDto createItemDto
    ) {
        Item item = itemService.upsertItem(createItemDto);
        return ResponseEntity.ok(item);
    }
}
