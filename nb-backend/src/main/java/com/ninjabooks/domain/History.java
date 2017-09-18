package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represent history table in database.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "HISTORY")
public class History extends BaseEntity
{
    @Column(name = "RETURN_DATE")
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public History() {
    }

    public History(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(returnDate, history.returnDate) &&
            Objects.equals(book, history.book) &&
            Objects.equals(user, history.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnDate, book, user);
    }

    @Override
    public String toString() {
        return "History{" +
            "returnDate=" + returnDate +
            ", book=" + book +
            ", user=" + user +
            '}';
    }
}
