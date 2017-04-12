package com.ninjabooks.domain;

import javax.persistence.*;
import java.util.List;

/**
 * This class represent book in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @author Andrzej Zajst
 * @since 1.0
 */
@Entity
@Table(name = "BOOK")
public class Book extends BaseEntity
{
    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "ISBN")
    private String isbn;

    @OneToOne
    private QRCode QR_code;

    @OneToMany(mappedBy = "book")
    private List<Queue> queues;

    @OneToMany(mappedBy = "book")
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "book")
    private List<History> histories;

    public Book() {
    }

    /**
     * Create new istance of book object
     *
     * @param title
     * @param author
     * @param isbn
     *
     */

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public QRCode getQR_code() {
        return QR_code;
    }

    public void setQR_code(QRCode QR_code) {
        this.QR_code = QR_code;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
