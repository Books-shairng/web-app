package com.ninjabooks.dao.db;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class DBBorrowDao implements GenericDao<Borrow, Long>
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
//        this.currentSession = sessionFactory.getCurrentSession();
    }

    @Override
    public Stream<Borrow> getAll() {
        return currentSession.createQuery("SELECT b FROM Borrow b",Borrow.class).stream();
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
        currentSession.delete(id);
    }
}
