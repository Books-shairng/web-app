package com.ninjabooks.dao;

import com.ninjabooks.domain.Book;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public interface BookDao extends GenericDao<Book, Long>
{
    public Book getByTitle(String title);
    public Book getByAuthor(String author);
    public Book getByISBN(String isbn);

}
