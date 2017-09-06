package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBBookDao implements BookDao
{
    private enum DBColumnName {TITLE, AUTHOR, ISBN}

    private final SessionFactory sessionFactory;
    private final DBDaoHelper<Book> daoHelper;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBBookDao(SessionFactory sessionFactory, DBDaoHelper<Book> daoHelper, @Qualifier(value = "streamFinder")
        SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<Book> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("select book from com.ninjabooks.domain.Book book", Book.class).stream();
    }

    @Override
    public Optional<Book> getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        Book book = currentSession.get(Book.class, id);
        return Optional.ofNullable(book);
    }

    @Override
    public Stream<Book> getByTitle(String title) {
        return specifiedElementFinder.findSpecifiedElementInDB(title, DBColumnName.TITLE);
    }

    @Override
    public Stream<Book> getByAuthor(String author) {
        return specifiedElementFinder.findSpecifiedElementInDB(author, DBColumnName.AUTHOR);
    }

    @Override
    public Stream<Book> getByISBN(String isbn) {
        return specifiedElementFinder.findSpecifiedElementInDB(isbn, DBColumnName.ISBN);
    }

    @Override
    public void add(Book book) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(book);
        currentSession.close();
    }

    @Override
    public void update(Book book) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.update(book);
    }

    @Override
    public void delete(Book book) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.delete(book);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

//    @Override
//    @SuppressWarnings("unchecked t cast")
//    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
//        Session currentSession = sessionFactory.openSession();
//        String query = "select book from com.ninjabooks.domain.Book book where " + columnName + "=:parameter";
//        Query<Book> bookQuery = currentSession.createQuery(query, Book.class);
//        bookQuery.setParameter("parameter", parameter);
//        return (T) bookQuery.stream();
//    }
}
