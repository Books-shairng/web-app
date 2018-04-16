package com.ninjabooks.json.book;

import com.ninjabooks.validator.isbn.ISBN;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRequest implements Serializable
{
    private static final long serialVersionUID = 1636948648964665823L;

    @NotEmpty(message = "{default.NotEmpty.message}")
    private final String title;

    @NotEmpty(message = "{default.NotEmpty.message}")
    private final String author;

    @NotEmpty(message = "{default.NotEmpty.message}")
    @ISBN
    private final String isbn;

    @NotEmpty(message = "{default.NotEmpty.message}")
    @Length(max = 5000, message = "{description.Length.message}")
    private final String description;

    @JsonCreator
    public BookRequest(@JsonProperty(value = "title") String title,
                       @JsonProperty(value = "author") String author,
                       @JsonProperty(value = "isbn") String isbn,
                       @JsonProperty(value = "description") String description) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }
}
