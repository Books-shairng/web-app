package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
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
    private Session currentSession;

    @Autowired
    public DBBorrowDao(SessionFactory sessionFactory) {
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
    public Stream<Borrow> getAll() {
        return currentSession.createQuery("SELECT b FROM com.ninjabooks.domain.Borrow b",Borrow.class).stream();
    }

    @Override
    public Borrow getById(Long id) {
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
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        String query = "select borrow from com.ninjabooks.domain.Borrow borrow where " + columnName + "=:parameter";
        Query<Borrow> bookQuery = currentSession.createQuery(query, Borrow.class);
        bookQuery.setParameter("parameter", parameter);

        return (T) bookQuery.getSingleResult();
    }
}
