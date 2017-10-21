//package com.ninjabooks.controller;
//
//import com.ninjabooks.domain.Book;
//import com.ninjabooks.domain.QRCode;
//import com.ninjabooks.error.borrow.BorrowException;
//import com.ninjabooks.error.qrcode.QRCodeException;
//import com.ninjabooks.error.qrcode.QRCodeNotFoundException;
//import com.ninjabooks.service.rest.borrow.BookRentalService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author Piotr 'pitrecki' Nowak
// * @since 1.0
// */
////todo refactoring this shit
//@RestController
//@ResponseStatus(value = HttpStatus.OK)
//@RequestMapping(value = "/api/borrow/")
//public class BorrowController
//{
//    private final BookRentalService borrowService;
//
//    @Autowired
//    public BorrowController(BookRentalService borrowService) {
//        this.borrowService = borrowService;
//    }
//
//    @RequestMapping(value = "{userID}", method = RequestMethod.POST)
//    public void borrowBookUsingWebClient(@PathVariable Long userID, @RequestBody Book book) throws BorrowException {
//        borrowService.borrowBook(userID, book);
//    }
//
//    @RequestMapping(value = "{userID}", method = RequestMethod.PUT)
//    public void borrowBookUsingMobileClient(@PathVariable Long userID, @RequestBody QRCode qrCode) throws QRCodeNotFoundException, BorrowException {
//        borrowService.borrowBook(userID, qrCode);
//    }
//
//    @RequestMapping(value = "/confirm/", method = RequestMethod.POST)
//    public void confirmBorrow(@RequestBody QRCode qrCode) throws QRCodeException {
//        borrowService.confirmBorrow(qrCode);
//    }
//
//    @RequestMapping(value = "/return/", method = RequestMethod.POST)
//    public void returnBook(@RequestBody QRCode qrCode) throws QRCodeException {
//        borrowService.returnBook(qrCode);
//    }
//
//    @RequestMapping(value = "/extend/{userID}", method = RequestMethod.POST)
//    public void extendReturnDateUsingWebClient(@PathVariable Long userID, @RequestBody Book book) throws BorrowException {
//        borrowService.extendReturnDate(userID, book);
//    }
//    @RequestMapping(value = "/extend/", method = RequestMethod.PUT)
//    public void extendReturnDateUsingMobileClient(@RequestBody QRCode qrCode) throws BorrowException, QRCodeNotFoundException {
//        borrowService.extendReturnDate(qrCode);
//    }
//}
