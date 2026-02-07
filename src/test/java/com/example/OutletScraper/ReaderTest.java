package com.example.OutletScraper;

import com.example.OutletScraper.dto.CreateItemDto;
import com.example.OutletScraper.fileReaders.TxtDataReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    @Test
    void readsMultipleLinesCorrectly() throws Exception {
        String input = """
        https://example.com data1
        https://openai.com data2
        """;

        TxtDataReader reader = new TxtDataReader();
        List<CreateItemDto> result = reader.read(new StringReader(input));

        assertEquals(2, result.size());
        assertEquals("https://example.com", result.get(0).getUrl());
        assertEquals("data1", result.get(0).getSize());
    }

    @Test
    void throwsExceptionWhenLineIsInvalid() {
        TxtDataReader reader = new TxtDataReader();
        String input =
                "asd";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reader.read(new StringReader(input))
        );

        System.out.println(exception.getMessage());

    }
}
