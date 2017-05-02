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
public class DBBookDao implements BookDao, SpecifiedElementFinder
{
    private final static Logger logger = LogManager.getLogger(DBBookDao.class);

    private enum DBColumnName {TITLE, AUTHOR, ISBN}

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
        return findSpecifiedElementInDB(title, DBColumnName.TITLE);
    }

    @Override
    public Stream<Book> getByAuthor(String author) {
        return findSpecifiedElementInDB(author, DBColumnName.AUTHOR);
    }

    @Override
    public Stream<Book> getByISBN(String isbn) {
        return findSpecifiedElementInDB(isbn, DBColumnName.ISBN);
    }

    @Override
    public void add(Book book) {
        currentSession.save(book);
    }

    @Override
    public void update(Long id) {
        Book book = getById(id);
        currentSession.update(book);
    }

    @Override
    public void delete(Long id) {
        Book book = getById(id);
        currentSession.delete(book);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        String query = "select book from com.ninjabooks.domain.Book book where " + columnName + "=:parameter";
        Query<Book> bookQuery = currentSession.createQuery(query, Book.class);
        bookQuery.setParameter("parameter", parameter);

        return (T) bookQuery.stream();
    }
}
