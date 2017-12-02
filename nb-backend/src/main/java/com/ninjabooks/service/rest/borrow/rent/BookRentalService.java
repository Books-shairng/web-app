package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;

/**
 * This service is responsible for handling book borrows.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookRentalService
{
    /**
     * It simple borrow book by user
     *
     * @param userID     - current user
     * @param qrCodeData - scanned QR code
     */

    void rentBook(Long userID, String qrCodeData) throws QRCodeException, BorrowException;

}
