package com.ninjabooks.domain;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

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

    @Column(name = "REAL_RETURN_DATE")
    private LocalDate realReturnDate;

    @Column(name = "EXPECTED_RETURN_DATE")
    private LocalDate expectedReturnDate;

    @Column(name = "CAN_EXTEND_RETURN_DATE")
    private boolean canExtendBorrow = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
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
        calculateExpectedReturnDate(borrowDate);
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
        calculateExpectedReturnDate(borrowDate);
    }

    public LocalDate getRealReturnDate() {
        return realReturnDate;
    }

    public void setRealReturnDate(LocalDate realReturnDate) {
        this.realReturnDate = realReturnDate;
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

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public boolean getCanExtendBorrow() {
        return canExtendBorrow;
    }

    public void setCanExtendBorrow(boolean canExtendBorrow) {
        this.canExtendBorrow = canExtendBorrow;
    }

    public void extendReturnDate() {
        LocalDate tmpDate = LocalDate.now().plusWeeks(2);
        expectedReturnDate = checkIfReturnDateEndOnWeekend(tmpDate);
    }

    private void calculateExpectedReturnDate(LocalDate borrowDate) {
        LocalDate tmpDate = borrowDate.plusDays(30);
        expectedReturnDate = checkIfReturnDateEndOnWeekend(tmpDate);
    }

    private LocalDate checkIfReturnDateEndOnWeekend(LocalDate returnDate) {
        if (returnDate.getDayOfWeek() == DayOfWeek.SATURDAY || returnDate.getDayOfWeek() == DayOfWeek.SUNDAY)
            return returnDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        return returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrow borrow = (Borrow) o;
        return
            Objects.equals(this.getId(), borrow.getId()) &&
            canExtendBorrow == borrow.canExtendBorrow &&
            Objects.equals(borrowDate, borrow.borrowDate) &&
            Objects.equals(realReturnDate, borrow.realReturnDate) &&
            Objects.equals(expectedReturnDate, borrow.expectedReturnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowDate, realReturnDate, expectedReturnDate, canExtendBorrow);
    }

    @Override
    public String toString() {
        return "Borrow{" +
            "borrowDate=" + borrowDate +
            ", realReturnDate=" + realReturnDate +
            ", expectedReturnDate=" + expectedReturnDate +
            ", canExtendBorrow=" + canExtendBorrow +
            '}';
    }
}
