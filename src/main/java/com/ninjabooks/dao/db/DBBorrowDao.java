package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
public class DBBorrowDao implements BorrowDao, SpecifiedElementFinder
{
    private final  static Logger logger = LogManager.getLogger(DBBorrowDao.class);

    private enum DBColumnName {BORROW_DATE, RETURN_DATE}

    private final SessionFactory sessionFactory;

    @Autowired
    public DBBorrowDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Stream<Borrow> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT b FROM com.ninjabooks.domain.Borrow b", Borrow.class).stream();
    }

    @Override
    public Borrow getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        return currentSession.get(Borrow.class, id);
    }

    @Override
    public Borrow getByReturnDate(LocalDate returnDate) {
        return findSpecifiedElementInDB(returnDate, DBColumnName.RETURN_DATE);
    }

    @Override
    public Borrow getByBorrowDate(LocalDate borrowDate) {
        return findSpecifiedElementInDB(borrowDate, DBColumnName.BORROW_DATE);
    }

    @Override
    public void add(Borrow borrow) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(borrow);
        currentSession.close();
    }

    @Override
    public void update(Borrow borrow) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.update(borrow);
        currentSession.getTransaction().commit();
        currentSession.close();
    }

    @Override
    public void delete(Borrow borrow) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.delete(borrow);
        currentSession.getTransaction().commit();
        currentSession.close();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        Session currentSession = sessionFactory.openSession();
        String query = "select borrow from com.ninjabooks.domain.Borrow borrow where " + columnName + "=:parameter";
        Query<Borrow> bookQuery = currentSession.createQuery(query, Borrow.class);
        bookQuery.setParameter("parameter", parameter);

        if (bookQuery.getSingleResult() != null) {
            return (T) bookQuery.getSingleResult();
        }
        return null;
    }
}
