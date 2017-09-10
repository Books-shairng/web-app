package com.ninjabooks.service.rest.order;

import com.ninjabooks.error.order.OrderException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface OrderBookService
{
    /**
     * Order title chosen by user,
     *  @param userID - current user id
     *  @param bookID - book ID which will be ordered by user
     */

    void orderBook(Long userID, Long bookID) throws OrderException;
}
