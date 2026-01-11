package org.example.model;

public class OutPutArticle {
    private String name;
    private String size;
    private double price;
    private boolean available;

    public OutPutArticle(String name, boolean available, double price, String size) {
        this.name = name;
        this.available = available;
        this.price = price;
        this.size = size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OutPutArticle{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}


