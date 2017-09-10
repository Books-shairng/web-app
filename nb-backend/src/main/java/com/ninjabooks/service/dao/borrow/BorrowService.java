package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.error.qrcode.QRCodeException;
import com.ninjabooks.error.qrcode.QRCodeNotFoundException;

/**
 * This service is responsible for handling book borrows.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
//todo refactoring this shit
public interface BorrowService
{
    /**
     * -- Web client --
     *
     * Borrow status is false, return date not set,
     * wait on confirm by mobile client
     * {@link #confirmBorrow(QRCode)}
     *
     * @param userID - current user
     * @param book - which user want borrow
     */

    void borrowBook(Long userID, Book book) throws BorrowException;

    /**
     * -- Mobile client --
     *
     * Automacally sets borrow status on true and borrow date on now.
     *
     * @param userID - current user
     * @param qrCode - scanned qr code found on the book
     */

    void borrowBook(Long userID, QRCode qrCode) throws QRCodeNotFoundException, BorrowException;

    /**
     * User can once extend return date by two weeks.
     *
     * @param userID - current user
     * @param book - which should be extended
     */

    void extendReturnDate(Long userID, Book book) throws BorrowException;

    /**
     * User can once extend return by two weeks.
     * Functionality for mobile users.
     *
     * @param qrCode - scanned by user
     */

    void extendReturnDate(QRCode qrCode) throws BorrowException, QRCodeNotFoundException;

    /**
     * Return book and changes borrowed status to false.
     * After this operation, the record goes to history and becomes the candidate
     * to be removed.
     *
     * @param qrCode - of book that user wants to return
     */

    void returnBook(QRCode qrCode) throws QRCodeException;

    /**
     * This method is used, when user has borrowed a book by web client.
     * If QR code is correct and match with book then borrow status is true and return
     * date is calculated.
     *
     * @param qrCode - tagged on the book and scanned by mobile client
     */

    void confirmBorrow(QRCode qrCode) throws QRCodeException;


}
