package com.example.OutletScraper.service;


import com.example.OutletScraper.dto.CreateArticleDTO;
import com.example.OutletScraper.model.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FileParser {

    private static final String FILE_PATH = "src/main/resources/data/list.txt";

    private static final String REGEX_PATTERN = "(https:\\/\\/)?www.mangooutlet.com\\/ro";


    private void checkValidUrl(String url) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        if (matchFound) {
            throw new RuntimeException("Not a valid mangoutlet URL! Should be: mangooutlet.com/ro");
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
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
                checkValidUrl(array[0]);
                line = br.readLine();
                CreateArticleDTO createArticleDTO = new CreateArticleDTO(array[0], array[1]);
                log.info("Created articleDTO" + createArticleDTO.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
