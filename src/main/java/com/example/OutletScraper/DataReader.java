package com.example.OutletScraper;

import com.example.OutletScraper.dto.CreateItemDto;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface DataReader {
    public List<CreateItemDto> read(Reader reader)throws IOException;
}
