package com.example.OutletScraper.helpers;

import com.example.OutletScraper.model.Size;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ValidationHelpers {

    private static final String REGEX_PATTERN = "(https:\\/\\/)?www.mangooutlet.com\\/ro";

    public void checkValidUrl(String url) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        if (!matchFound) {
            throw new RuntimeException("Not a valid mangoutlet URL! Should be: mangooutlet.com/ro");
        }
    }

    public void checkCorrectSize(String size) {
        try {
            Size.valueOf(size.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid size: " + size, e);
        }

    }
}
