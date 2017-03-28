package com.ninjabooks.dao;

import com.ninjabooks.domain.Queue;
import com.ninjabooks.util.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBQueueDao implements GenericDao<Queue>
{
    private final Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();

    public DBQueueDao() {
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
