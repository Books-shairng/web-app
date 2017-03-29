package com.ninjabooks.dao.db;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.History;
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
public class DBHistoryDao implements GenericDao<History, Long>
{
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBHistoryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            currentSession = sessionFactory.openSession();
        }
//        this.currentSession = sessionFactory.getCurrentSession();
    }

    @Override
    public Stream<History> getAll() {
        return currentSession.createQuery("SELECT h FROM History h", History.class).stream();
    }

    @Override
    public History getById(Long id) {
        return currentSession.get(History.class, id);
    }

    @Override
    public void add(History history) {
        currentSession.save(history);
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
