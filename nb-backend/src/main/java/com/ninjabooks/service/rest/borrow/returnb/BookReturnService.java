package com.ninjabooks.service.rest.borrow.returnb;

import com.ninjabooks.error.borrow.BorrowException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookReturnService
{
    /**
     * Return book and changes borrowed status to false.
     * After this operation, the record goes to history and becomes the candidate
     * to be removed.
     *
     * @param qrCode - of book that user wants to return
     */

    void returnBook(String qrCode) throws BorrowException;
}
