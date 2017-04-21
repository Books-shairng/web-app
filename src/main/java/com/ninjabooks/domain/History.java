package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * This class represent history table in database.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 *
 * <b> EMPTY </b>
 */
@Entity
@Table(name = "HISTORY")
public class History extends BaseEntity
{

    @Column(name = "BORROW_DATE")
    private LocalDate borrowDate;

    @Column(name = "RETURNED_DATE")
    private LocalDate returnedDate;

    @Column(name = "COMMENT", length = 1200)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public History() {
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnDate) {
        this.returnedDate = returnDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + getId() +
                '}';
    }
}
