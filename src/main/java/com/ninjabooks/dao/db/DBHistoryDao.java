package com.ninjabooks.dao.db;

import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class DBHistoryDao implements HistoryDao
{
    private final static Logger logger = LogManager.getLogger(DBHistoryDao.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public DBHistoryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public History getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        return currentSession.get(History.class, id);
    }

    @Override
    public Stream<History> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT h FROM com.ninjabooks.domain.History h", History.class).stream();
    }

    @Override
    public void add(History history) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(history);
    }

    @Override
    public void update(History history) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.update(history);
        currentSession.getTransaction().commit();
    }

    @Override
    public void delete(History history) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.delete(history);
        currentSession.getTransaction().commit();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }
}
