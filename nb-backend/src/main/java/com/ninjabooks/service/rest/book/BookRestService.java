package com.ninjabooks.service.rest.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.json.book.BookInfo;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookRestService
{
    /**
     * Add new book to database, if operation succeed then return string with generated
     * qr code data.
     *
     * @param book - which will be added to system
     * @throws QRCodeException when cannot generate unique qr code
     * @return string with generated qr code
     */

    String addBook(Book book) throws QRCodeException;

    BookInfo getBookInfo(Long id);
}
