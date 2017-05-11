package com.restapi2017.model;

import java.math.BigDecimal;

public class Book {
    public Book(String id, String title, String authors, String description, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.price = price;
    }

    public Book() {
    }

    private String id;

    private String title;

    private String authors;

    private String description;

    private BigDecimal price;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
