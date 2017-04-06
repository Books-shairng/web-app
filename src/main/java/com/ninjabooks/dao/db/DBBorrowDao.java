package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBBorrowDao implements BorrowDao
{
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBBorrowDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            currentSession = sessionFactory.openSession();
        }
    }

    @Override
    public Stream<Borrow> getAll() {
        return currentSession.createQuery("SELECT b FROM com.ninjabooks.domain.Borrow b",Borrow.class).stream();
    }

    @Override
    public Borrow getById(Long id) {
        return currentSession.get(Borrow.class, id);
    }

    @Override
    public void add(Borrow borrow) {
        currentSession.save(borrow);
    }

    @Override
    public void update(Long id) {
        currentSession.update(id);
    }

    @Override
    public void delete(Long id) {
        Borrow borrow =currentSession.get(Borrow.class, id);
        currentSession.delete(borrow);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }

    @Override
    public Borrow getByReturnDate(LocalDate returnDate) {
        return currentSession.get(Borrow.class, returnDate);
    }

    @Override
    public Borrow getByBorrowDate(LocalDate borrowDate) {
        return currentSession.get(Borrow.class, borrowDate);
    }
}
