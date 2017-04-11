package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.domain.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBBookDao implements BookDao
{
    private final static Logger logger = LogManager.getLogger(DBBookDao.class);

    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBBookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            logger.info("Try obtain current session");
            this.currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            logger.error(e);
            logger.info("Open new session");
            this.currentSession = sessionFactory.openSession();
        }
    }

    @Override
    public Stream<Book> getAll() {
        return currentSession.createQuery("select book from com.ninjabooks.domain.Book book",Book.class).stream();
    }

    @Override
    public Book getById(Long id) {
        return currentSession.get(Book.class, id);
    }

    @Override
    public Stream<Book> getByTitle(String title) {
        String query = "select book from com.ninjabooks.domain.Book book where TITLE =:title";
        return currentSession.createQuery(query,Book.class).setParameter("title", title).stream();
    }

    @Override
    public Stream<Book> getByAuthor(String author) {
        String query = "select book from com.ninjabooks.domain.Book book where AUTHOR =:author";
        return currentSession.createQuery(query, Book.class).setParameter("author", author).stream();
    }

    @Override
    public Stream<Book> getByISBN(String isbn) {
        String query = "select book from com.ninjabooks.domain.Book book where ISBN =:isbn";
        Query<Book> isbnQuery = currentSession.createQuery(query, Book.class);
        isbnQuery.setParameter("isbn", isbn);

        return isbnQuery.stream();
    }

    @Override
    public void add(Book book) {
        currentSession.save(book);
    }

    @Override
    public void update(Long id) {
        currentSession.update(id);
    }

    @Override
    public void delete(Long id) {
        Book book = currentSession.get(Book.class, id);
        currentSession.delete(book);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }
}
