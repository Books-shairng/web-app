package com.ninjabooks.controller;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.BorrowException;
import com.ninjabooks.error.QRCodeException;
import com.ninjabooks.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
@ResponseStatus(value = HttpStatus.OK)
@RequestMapping(value = "/api/borrow/")
public class BorrowController
{
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @RequestMapping(value = "{userID}", method = RequestMethod.POST)
    public void borrowBook(@PathVariable Long userID, @RequestBody Book book) throws BorrowException{
        borrowService.borrowBook(userID, book);
    }

    @RequestMapping(value = "/confirm/{userID}", method = RequestMethod.POST)
    public void confirmBorrow(@PathVariable Long userID, @RequestBody QRCode qrCode) throws QRCodeException{
        borrowService.confirmBorrow(userID, qrCode);
    }
}
