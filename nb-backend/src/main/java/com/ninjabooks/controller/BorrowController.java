package com.ninjabooks.controller;

import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.error.qrcode.QRCodeException;
import com.ninjabooks.service.rest.borrow.extend.ExtendRentalService;
import com.ninjabooks.service.rest.borrow.rent.BookRentalService;
import com.ninjabooks.service.rest.borrow.returnb.BookReturnService;
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
    private final BookRentalService borrowService;
    private final BookReturnService returnService;
    private final ExtendRentalService extendRentalService;

    @Autowired
    public BorrowController(BookRentalService borrowService, BookReturnService returnService,
                            ExtendRentalService extendRentalService) {
        this.borrowService = borrowService;
        this.returnService = returnService;
        this.extendRentalService = extendRentalService;
    }

    @RequestMapping(value = "{userID}/", method = RequestMethod.POST)
    public void borrowBook(@PathVariable(name = "userID") Long userID,
                           @RequestParam(value = "qrCode") String qrCode) throws QRCodeException, BorrowException {
        borrowService.rentBook(userID, qrCode);
    }

    @RequestMapping(value = "return/", method = RequestMethod.POST)
    public void returnBook(@RequestParam(value = "qrCode") String qrCode) throws BorrowException {
        returnService.returnBook(qrCode);
    }

    @RequestMapping(value = "{userID}/extend/", method = RequestMethod.POST)
    public void extendReturnDate(@PathVariable(name = "userID") Long userID,
                                 @RequestParam(value = "bookID") Long bookID) throws BorrowException {
        extendRentalService.extendReturnDate(userID, bookID);
    }
}
