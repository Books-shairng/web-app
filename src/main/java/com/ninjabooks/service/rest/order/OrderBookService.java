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
     * @param title - title which will be ordered
     */

    void orderBook(Long userID, String title) throws OrderException;
}
