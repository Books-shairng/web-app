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
    private Session currentSession;

    @Autowired
    public DBHistoryDao(SessionFactory sessionFactory) {
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
    public History getById(Long id) {
        return currentSession.get(History.class, id);
    }

    @Override
    public Stream<History> getAll() {
        return currentSession.createQuery("SELECT h FROM com.ninjabooks.domain.History h", History.class).stream();
    }

    @Override
    public void add(History history) {
        currentSession.save(history);
    }

    @Override
    public void update(Long id) {
        History history = getById(id);
        currentSession.update(history);
    }

    @Override
    public void delete(Long id) {
        History history = getById(id);
        currentSession.delete(history);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }
}
