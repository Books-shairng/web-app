package com.ninjabooks.service.dao.book;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BookDaoServiceImpl extends GenericServiceImpl<Book, Long> implements BookDaoService
{
    private final BookDao bookDao;

    @Autowired
    public BookDaoServiceImpl(GenericDao<Book, Long> genericDao, BookDao bookDao) {
        super(genericDao);
        this.bookDao = bookDao;
    }

    @Override
    public Stream<Book> getByTitle(String title) {
        return bookDao.getByTitle(title);
    }

    @Override
    public Stream<Book> getByAuthor(String author) {
        return bookDao.getByAuthor(author);
    }

    @Override
    public Stream<Book> getByISBN(String isbn) {
        return bookDao.getByISBN(isbn);
    }
}
