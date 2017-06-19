package com.ninjabooks.service;

import com.ninjabooks.domain.Book;
import com.ninjabooks.error.QRCodeException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookService
{
    /**
     * Add new book to database, if operation succeed then return string with generated
     * qr code data.
     *
     * @param book - which will be added to system
     * @return string with generated qr code
     */

    String addBook(Book book) throws QRCodeException;

    /**
     * Return book by specified id.
     *
     * @param id - of the book which will fetched
     * @return the searched book
     */

    Book getBookById(Long id);
}
