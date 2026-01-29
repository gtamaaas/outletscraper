package com.example.OutletScraper.service;


import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.helpers.ValidationHelpers;
import com.example.OutletScraper.model.Item.Item;
import com.example.OutletScraper.model.Item.Size;
import com.example.OutletScraper.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class FileParser {


    private final ValidationHelpers validationhelpers;

    public FileParser(ValidationHelpers validationhelpers, ItemRepository itemRepository) {
        this.validationhelpers = validationhelpers;
    }


    public List<CreateItemDto> readFile(String file) {
        log.info("Opening file");
        int lineCount = 0;
        List<CreateItemDto> listOfCreateItemDtos = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
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
                CreateItemDto createItemDto = new CreateItemDto(array[0], Size.valueOf(array[1]));
                listOfCreateItemDtos.add(createItemDto);
                line = br.readLine();
                log.info("Created itemDTO" + createItemDto.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listOfCreateItemDtos;

    }
}
