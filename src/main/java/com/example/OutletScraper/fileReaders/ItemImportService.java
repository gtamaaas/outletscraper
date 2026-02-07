package com.example.OutletScraper.fileReaders;

import com.example.OutletScraper.dto.CreateItemDto;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class ItemImportService {

    private final DataReader dataReader;

    // Inject the DataReader through constructor
    public ItemImportService(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public List<CreateItemDto> loadItemsFromFile(String filePath) throws IOException {
        try (FileReader fileReader = new FileReader(filePath)) {
            return dataReader.read(fileReader);
        }
    }
}
