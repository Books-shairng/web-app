package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Book;

/**
 * Notification by default should return information about book.
 * If notification should return other information like order date, this class
 * must be inherited by more elaborate class.
 *
 * @see BorrowNotification
 * @see QueueNotification
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class GenericNotification
{
    private String author;
    private String title;
    private String isbn;

    public GenericNotification() {
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

    void obtainBookFromGenericType(Book book) {
        author = book.getAuthor();
        title = book.getTitle();
        isbn = book.getIsbn();
    }
}
