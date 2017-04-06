package com.ninjabooks.dao;

import com.ninjabooks.domain.Book;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookDao extends GenericDao<Book, Long>
{
    Book getByTitle(String title);

    Book getByAuthor(String author);

    Book getByISBN(String isbn);

}
