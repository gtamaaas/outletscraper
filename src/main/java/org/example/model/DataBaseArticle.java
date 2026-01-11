package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class DataBaseArticle {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private String size;
    private double price;
    private LocalDateTime timeStamp;
    private boolean IsAvailable;

    public DataBaseArticle(String name, String size, LocalDateTime timeStamp, double price, boolean isAvailable) {
        this.name = name;
        this.size = size;
        this.timeStamp = timeStamp;
        this.price = price;
        IsAvailable = isAvailable;
    }
}
