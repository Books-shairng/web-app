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

    @Autowired
    public DBQueueDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;}

    @Override
    public Stream<Queue> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.Queue q", Queue.class).stream();
    }

    @Override
    public Queue getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        return currentSession.get(Queue.class, id);
    }

    @Override
    public Queue getByOrderDate(LocalDateTime orderDate) {
        return findSpecifiedElementInDB(orderDate, DBColumnName.ORDER_DATE);
    }

    @Override
    public void add(Queue queue) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(queue);
    }

    @Override
    public void update(Queue queue) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.update(queue);
        currentSession.getTransaction().commit();
    }

    @Override
    public void delete(Queue queue) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.delete(queue);
        currentSession.getTransaction().commit();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        Session currentSession = sessionFactory.openSession();
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
