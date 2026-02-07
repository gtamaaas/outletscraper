package com.example.OutletScraper.service;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.fileReaders.ItemImportService;
import com.example.OutletScraper.model.Item;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ScheduleService {

    private ItemService itemService;
    private ItemImportService itemImportService;

    private String filePath = "src/main/resources/data/list.txt";

    public ScheduleService(ItemService itemService, ItemImportService itemImportService) {
        this.itemService = itemService;
        this.itemImportService = itemImportService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledUpsertItemsFromList() {
        try {
            List<CreateItemDto> items = itemImportService.loadItemsFromFile(filePath);
            List<Item> createdOrUpdatedItems = itemService.upsertItems(items);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
