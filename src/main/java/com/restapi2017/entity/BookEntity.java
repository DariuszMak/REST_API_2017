package com.restapi2017.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
@NamedQueries({
        @NamedQuery(name = "books.findAll", query = "SELECT b FROM BookEntity b")
})
public class BookEntity extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(name = "title")
    @NotNull
    @Size(min = 1, max = 40)
    private String title;

    @Column(name = "authors")
    @NotNull
    @Size(min = 1, max = 30)
    private String authors;

    @Column(name = "description")
    @NotNull @Size(min = 1, max = 100)
    private String description;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    public BookEntity(){

    }

    public BookEntity(String title, String authors, String description, BigDecimal price) {
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
