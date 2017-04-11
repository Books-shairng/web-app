package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * This class represent borrow in databes
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

    private void calculateReturnDate(LocalDate borrowDate) {
        returnDate = borrowDate.plusDays(30);
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
