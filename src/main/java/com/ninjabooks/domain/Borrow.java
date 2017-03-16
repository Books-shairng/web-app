package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * This class represent borrow in databes
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "Borrow")
public class Borrow extends BaseEntity
{
    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "return_date")
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
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    private void calculateReturnDate(LocalDate borrowDate) {
        returnDate = borrowDate.plusDays(30);
    }
}
