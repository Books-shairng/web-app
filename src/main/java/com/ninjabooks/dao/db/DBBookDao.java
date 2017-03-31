package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.domain.Book;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBBookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            this.currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
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
