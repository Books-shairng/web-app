package com.ninjabooks.controller;

import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.service.rest.order.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class OrderController
{
    private final OrderBookService orderBookService;

    @Autowired
    public OrderController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/api/order/{userID}/", method = RequestMethod.POST)
    public void orderBook(@PathVariable Long userID,
                          @RequestParam(value = "bookID") Long bookID)
        throws OrderException {
        orderBookService.orderBook(userID, bookID);
    }
}
