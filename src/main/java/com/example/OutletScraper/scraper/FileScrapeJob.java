package com.example.OutletScraper.scraper;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.service.FileParser;
import com.example.OutletScraper.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FileScrapeJob {

    private final FileParser fileParser;
    private final ItemService itemService;
    private final ScrapeSessionFactory sessionFactory;

    @Value("${scraper.input-file}")
    private String inputFile;

    public FileScrapeJob(
            FileParser fileParser,
            ItemService itemService,
            ScrapeSessionFactory sessionFactory) {

        this.fileParser = fileParser;
        this.itemService = itemService;
        this.sessionFactory = sessionFactory;
    }

    public int run() {

        List<CreateItemDto> items = fileParser.readFile(inputFile);

        try (ScrapeSession session =
                     new ScrapeSession(sessionFactory.open())) {

            for (CreateItemDto dto : items) {
                itemService.updateItem(dto, session.driver());
            }

        }

        return items.size();
    }
}

