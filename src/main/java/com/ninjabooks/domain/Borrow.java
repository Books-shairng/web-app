package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * This class represent borrow in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "BORROW")
public class Borrow extends BaseEntity
{
    @Column(name = "BORROW_DATE")
    private LocalDate borrowDate;

    @Column(name = "RETURN_DATE")
    private LocalDate returnDate;

    @Column(name = "STATUS")
    private boolean isBorrowed;

    @Column(name = "EXTENDED_RETURN_DATE")
    private boolean canExtendBorrow;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Borrow() {
    }

    /**
     * Create new instance of borrow object
     * @param borrowDate date when user borrow book
     */

    public Borrow(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        calculateReturnDate(borrowDate);
    }

    public Borrow(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        calculateReturnDate(borrowDate);
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


    public boolean getIsBorrowed() {
        return isBorrowed;
    }

    public void setIsBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }

    public boolean getCanExtendBorrow() {
        return canExtendBorrow;
    }

    public void setCanExtendBorrow(boolean canExtendBorrow) {
        this.canExtendBorrow = canExtendBorrow;
    }

    public void extendReturnDate() {
        LocalDate tmpDate = returnDate.plusDays(14);
        returnDate = checkIfReturnDateEndOnWeekend(tmpDate);
    }

    private void calculateReturnDate(LocalDate borrowDate) {
        LocalDate tmpDate = borrowDate.plusDays(30);

        returnDate = checkIfReturnDateEndOnWeekend(tmpDate);
    }

    public LocalDate checkIfReturnDateEndOnWeekend(LocalDate returnDate) {
        if (returnDate.getDayOfWeek() == DayOfWeek.SATURDAY || returnDate.getDayOfWeek() == DayOfWeek.SUNDAY)
            return returnDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        return returnDate;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + getId() +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
