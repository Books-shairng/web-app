package com.ninjabooks.controller;

import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.order.OrderBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public MessageResponse orderBook(@PathVariable(value = "userID") Long userID,
                                     @RequestParam(value = "bookID") Long bookID)
        throws OrderException {
        orderBookService.orderBook(userID, bookID);

        return new MessageResponse("Book was corectly ordered");
    }
}
