package com.ninjabooks.service.rest.borrow.extend;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface ExtendRentalService
{
    void extendReturnDate(Long userID, Long bookID);
}
