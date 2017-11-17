package com.ninjabooks.domain;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

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

    @Column(name = "COMMENTED")
    private boolean isCommented;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
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

    public boolean getIsCommented() {
        return isCommented;
    }

    public void setIsCommented(boolean commented) {
        isCommented = commented;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return
            Objects.equals(this.getId(), history.getId()) &&
            Objects.equals(returnDate, history.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnDate);
    }

    @Override
    public String toString() {
        return "History{" +
            "returnDate=" + returnDate +
            "isCommented=" + isCommented +
            '}';
    }
}
