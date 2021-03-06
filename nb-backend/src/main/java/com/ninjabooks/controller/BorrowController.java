package com.ninjabooks.controller;

import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.borrow.extend.ExtendRentalService;
import com.ninjabooks.service.rest.borrow.rent.BookRentalService;
import com.ninjabooks.service.rest.borrow.returnb.BookReturnService;

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
@ResponseStatus(value = HttpStatus.OK)
@RequestMapping(value = "/api/borrow/")
public class BorrowController
{
    private final BookRentalService borrowService;
    private final BookReturnService returnService;
    private final ExtendRentalService extendRentalService;

    @Autowired
    public BorrowController(BookRentalService borrowService,
                            BookReturnService returnService,
                            ExtendRentalService extendRentalService) {
        this.borrowService = borrowService;
        this.returnService = returnService;
        this.extendRentalService = extendRentalService;
    }

    @RequestMapping(value = "{userID}/", method = RequestMethod.POST)
    public MessageResponse borrowBook(@PathVariable(name = "userID") Long userID,
                                      @RequestParam(value = "qrCode") String qrCode)
        throws QRCodeException, BorrowException {
        borrowService.rentBook(userID, qrCode);

        return new MessageResponse("User successfully borrow book");
    }

    @RequestMapping(value = "return/", method = RequestMethod.POST)
    public MessageResponse returnBook(@RequestParam(value = "qrCode") String qrCode) throws BorrowException {
        returnService.returnBook(qrCode);

        return new MessageResponse("User successfully return book");
    }

    @RequestMapping(value = "{userID}/extend/", method = RequestMethod.POST)
    public MessageResponse extendReturnDate(@PathVariable(name = "userID") Long userID,
                                            @RequestParam(value = "bookID") Long bookID) throws BorrowException {
        extendRentalService.extendReturnDate(userID, bookID);

        return new MessageResponse("User successfully extend return date by two weeks");
    }
}
