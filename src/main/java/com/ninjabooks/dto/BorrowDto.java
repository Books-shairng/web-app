package com.ninjabooks.dto;

import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class BorrowDto
{
    private String borrowDate;
    private String returnDate;

    public BorrowDto() {
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate.toString();
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate.toString();
    }


}
