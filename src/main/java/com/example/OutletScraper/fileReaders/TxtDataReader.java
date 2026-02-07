package com.example.OutletScraper.fileReaders;

import com.example.OutletScraper.dto.CreateItemDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TxtDataReader implements DataReader {
    @Override
    public List<CreateItemDto> read(Reader reader) throws IOException {
        List<CreateItemDto> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid line: " + line);
                }
                System.out.println(parts[0] + " " + parts[1]);
                records.add(new CreateItemDto(parts[0], parts[1]));
            }
        }
        return records;
    }
}
