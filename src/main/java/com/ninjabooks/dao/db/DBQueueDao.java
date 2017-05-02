package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QueueDao;
import com.ninjabooks.domain.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBQueueDao implements QueueDao, SpecifiedElementFinder
{
    private final static Logger logger = LogManager.getLogger(DBQueueDao.class);

    private enum DBColumnName {ORDER_DATE}

    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBQueueDao(SessionFactory sessionFactory) {
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
    public Stream<Queue> getAll() {
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.Queue q", Queue.class).stream();
    }

    @Override
    public Queue getById(Long id) {
        return currentSession.get(Queue.class, id);
    }

    @Override
    public Queue getByOrderDate(LocalDateTime orderDate) {
        return findSpecifiedElementInDB(orderDate, DBColumnName.ORDER_DATE);
    }

    @Override
    public void add(Queue queue) {
        currentSession.save(queue);
    }

    @Override
    public void update(Long id) {
        Queue queue = getById(id);
        currentSession.update(queue);
    }

    @Override
    public void delete(Long id) {
        Queue queue = getById(id);
        currentSession.delete(queue);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        String query = "select queue from com.ninjabooks.domain.Queue queue where " + columnName + "=:parameter";
        Query<Queue> queueQuery = currentSession.createQuery(query, Queue.class);
        queueQuery.setParameter("parameter", parameter);

        List<Queue> results = queueQuery.getResultList();
        if (!results.isEmpty())
            return (T) results.get(0);
        else
            return null;
    }
}
