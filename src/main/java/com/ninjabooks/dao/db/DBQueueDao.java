package com.ninjabooks.dao.db;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Queue;
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
public class DBQueueDao implements GenericDao<Queue, Long>
{
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBQueueDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            currentSession = sessionFactory.openSession();
        }
//        this.currentSession = sessionFactory.getCurrentSession();
    }

    @Override
    public Stream<Queue> getAll() {
        return currentSession.createQuery("SELECT q FROM Queue q", Queue.class).stream();
    }

    @Override
    public Queue getById(Long id) {
        return currentSession.get(Queue.class, id);
    }

    @Override
    public void add(Queue queue) {
        currentSession.save(queue);
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
