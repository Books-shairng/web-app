package com.ninjabooks.json.comment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentResponse implements Serializable
{
    private static final long serialVersionUID = 4719413999741691927L;

    private String author;
    private LocalDateTime date;
    private String content;
    private String isbn;

    public CommentResponse(String author, LocalDateTime date, String content, String isbn) {
        this.author = author;
        this.date = date;
        this.content = content;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
