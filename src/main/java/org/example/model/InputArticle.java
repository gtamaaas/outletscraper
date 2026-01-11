package org.example.model;

public class InputArticle {
    private String URL;
    private String size;

    public InputArticle(String URL, String size) {
        this.URL = URL;
        this.size = size;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "InputArticle{" +
                "URL='" + URL + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
