package com.ninjabooks.dao.db;

import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DBHistoryDao implements HistoryDao
{
    private final SessionFactory sessionFactory;

    @Autowired
    public DBHistoryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<History> getById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        History history = currentSession.get(History.class, id);
        return Optional.ofNullable(history);
    }

    @Override
    public Stream<History> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT h FROM com.ninjabooks.domain.History h", History.class).stream();
    }

    @Override
    public void add(History history) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(history);
    }

    @Override
    public void update(History history) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(history);
    }

    @Override
    public void delete(History history) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(history);
    }


    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
