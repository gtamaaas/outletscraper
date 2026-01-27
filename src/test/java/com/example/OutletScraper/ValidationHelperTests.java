package com.example.OutletScraper;

import com.example.OutletScraper.helpers.ValidationHelpers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationHelperTests {

    private final ValidationHelpers validator = new ValidationHelpers();

    @Test
    void validSize_shouldNotThrow() {
        assertDoesNotThrow(() ->
                validator.checkCorrectSize("m")
        );
    }

    @Test
    void invalidSize_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.checkCorrectSize("not a szie")
        );

        assertTrue(exception.getMessage().contains("Invalid size"));
    }

    @Test
    void validMangoOutletUrl_shouldNotThrow() {
        assertDoesNotThrow(() ->
                validator
                        .checkValidUrl
                                ("https://www.mangooutlet.com/ro/ro/p/barbati/camasi/slim-fit/camasa-slim-fit-din-bumbac-chambray_87000608")
        );
    }

    @Test
    void invalidUrl_shouldThrowException() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> validator.checkValidUrl("https://mangooultet.com")
        );

        assertEquals(
                "Not a valid mangoutlet URL! Should be: mangooutlet.com/ro",
                exception.getMessage()
        );
    }
}
