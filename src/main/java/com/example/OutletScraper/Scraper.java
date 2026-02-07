package com.example.OutletScraper;

public interface Scraper {
    public String getItemName();
    public boolean isSizeAvailable(String size);
    public double getPrice();
    public int getDiscountPercent();
}
