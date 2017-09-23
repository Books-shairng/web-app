package com.ninjabooks.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ninjabooks.util.converter.json.LocalDateDeserializer;
import com.ninjabooks.util.converter.json.LocalDateSerializer;

import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class BorrowDto extends BaseEntityDto
{
    private static final long serialVersionUID = 3445982100156009090L;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate borrowDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expectedReturnDate;
    private boolean canExtendBorrow;

    public BorrowDto() {
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
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
}
