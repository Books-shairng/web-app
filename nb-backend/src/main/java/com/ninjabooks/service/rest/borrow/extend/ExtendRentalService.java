package com.ninjabooks.service.rest.borrow.extend;

import com.ninjabooks.error.exception.borrow.BorrowException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface ExtendRentalService
{

    /**
     * User should can once extend return date by two weeks.
     *
     * @param userID - current user
     * @param bookID - which should be extended
     */

    void extendReturnDate(Long userID, Long bookID) throws BorrowException;
}
