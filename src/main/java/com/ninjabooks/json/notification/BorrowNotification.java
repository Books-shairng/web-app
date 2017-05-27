package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Borrow;

/**
 * This class extend {@link GenericNotification} and add information about:
 * - borrow date
 * - return date
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowNotification extends GenericNotification
{
    private String borrowDate;
    private String returnDate;


    public BorrowNotification(Borrow borrow) {
        obtainBookFromGenericType(borrow.getBook());
        obtainDatesAsStrings(borrow);
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }


    private void obtainDatesAsStrings(Borrow borrow) {
        borrowDate = borrow.getBorrowDate().toString();
        returnDate = borrow.getReturnDate().toString();
    }
}
