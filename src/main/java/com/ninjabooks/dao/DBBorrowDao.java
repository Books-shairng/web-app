package com.ninjabooks.dao;

import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.HibernateUtil;
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
public class DBBorrowDao implements GenericDao<Borrow>
{
    private final Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();

    public DBBorrowDao() {
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
