package com.example.OutletScraper.repository;

import com.example.OutletScraper.model.Item.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findByUrl(String url);
}
