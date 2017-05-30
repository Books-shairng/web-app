package com.ninjabooks.service;

import com.ninjabooks.domain.Book;

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
     * @param book which will be added to system
     * @return string with generated qr code
     */

    String addBook(Book book);
}
