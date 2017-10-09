package com.ninjabooks.dao.db;

import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBHistoryDao implements HistoryDao
{
    private final SessionFactory sessionFactory;
    private final DBDaoHelper<History> daoHelper;

    @Autowired
    public DBHistoryDao(SessionFactory sessionFactory, DBDaoHelper<History> daoHelper) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
    }

    @Override
    public Optional<History> getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        History history = currentSession.get(History.class, id);
        return Optional.ofNullable(history);
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
        currentSession.close();
    }

    @Override
    public void update(History history) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.update(history);
    }

    @Override
    public void delete(History history) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.delete(history);
    }


    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }
}
