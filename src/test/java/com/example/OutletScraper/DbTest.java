package com.example.OutletScraper;


import com.example.OutletScraper.model.Item;
import com.example.OutletScraper.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@TestPropertySource(properties = {
        "spring.data.mongodb.database=outlet_scraper_test"
})
public class DbTest {

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void cleanUp() {
        itemRepository.deleteAll();
    }

    @Test
    void shouldSaveItem() {
        Item item = new Item();
        item.setName("shoe");
        item.setSize("big");

        Item savedItem = itemRepository.save(item);

        assertEquals("shoe", savedItem.getName());
        assertEquals("big", savedItem.getSize());

    }
}
