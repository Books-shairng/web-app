package com.ninjabooks.dao;

import com.ninjabooks.domain.Book;
import com.ninjabooks.util.HibernateUtil;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBBookDao implements GenericDao<Book>
{
    private final Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();

    public DBBookDao() {
    }

    @Override
    public Stream<Book> getAll() {
        return currentSession.createQuery("select b from BOOK b",Book.class).stream();
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
        currentSession.delete(id);
    }
}
