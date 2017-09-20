package com.ninjabooks.dto;

import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class BorrowDto extends BaseEntityDto
{
    private static final long serialVersionUID = 3445982100156009090L;

    private String borrowDate;
    private String expectedReturnDate;
    private boolean canExtendBorrow;

    public BorrowDto() {
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate.toString();
    }

    public String getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate.toString();
    }

    public boolean getCanExtendBorrow() {
        return canExtendBorrow;
    }

    public void setCanExtendBorrow(boolean canExtendBorrow) {
        this.canExtendBorrow = canExtendBorrow;
    }
}
