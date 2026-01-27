package com.example.OutletScraper.service;


import com.example.OutletScraper.dto.CreateArticleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class FileParser {

    private static final String FILE_PATH = "src/main/resources/data/list.txt";

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void readFile() {
        log.info("Opening file");
        int lineCount = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder sb = new StringBuilder();
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
                line = br.readLine();
                CreateArticleDTO createArticleDTO = new CreateArticleDTO(array[0], array[1]);
                log.info("Created articleDTO" + createArticleDTO.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
