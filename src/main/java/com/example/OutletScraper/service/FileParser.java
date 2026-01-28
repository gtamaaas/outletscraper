package com.example.OutletScraper.service;


import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.helpers.ValidationHelpers;
import com.example.OutletScraper.model.Article.Item;
import com.example.OutletScraper.model.Article.Size;
import com.example.OutletScraper.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
@Slf4j
public class FileParser {

    private static final String FILE_PATH = "src/main/resources/data/list.txt";


    private final ValidationHelpers validationhelpers;
    private final ItemRepository itemRepository;

    public FileParser(ValidationHelpers validationhelpers, ItemRepository itemRepository) {
        this.validationhelpers = validationhelpers;
        this.itemRepository = itemRepository;
    }

//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void readFile() {
        log.info("Opening file");
        int lineCount = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine();
            ++lineCount;
            while (line != null) {
                String[] array = line.split(" ");
                if(array.length != 2) {
                    throw new RuntimeException(
                            "Invalid line at "
                                    + lineCount +
                                    ": must contain exactly 2 words"
                    );
                }
                validationhelpers.checkValidUrl(array[0]);
                validationhelpers.checkCorrectSize(array[1]);
                CreateArticleDTO createArticleDTO = new CreateArticleDTO(array[0], Size.valueOf(array[1]));
                line = br.readLine();
                Item item = new Item();
                item.setUrl(createArticleDTO.getUrl());
                item.setSize(createArticleDTO.getSize());
                itemRepository.insert(item);
                log.info("Created articleDTO" + createArticleDTO.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
