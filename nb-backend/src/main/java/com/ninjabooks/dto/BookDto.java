package com.ninjabooks.dto;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookDto extends BaseEntityDto
{
    private static final long serialVersionUID = -6767045281479130800L;

    private String author;
    private String title;
    private String isbn;

    public BookDto() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
