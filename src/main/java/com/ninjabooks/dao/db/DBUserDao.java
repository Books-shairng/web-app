package com.ninjabooks.dao.db;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBUserDao implements UserDao
{
    private enum DBColumnName {NAME, EMAIL}

    private final SessionFactory sessionFactory;
    private final DBDaoHelper<User> daoHelper;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBUserDao(SessionFactory sessionFactory, DBDaoHelper<User> daoHelper, @Qualifier(value = "queryFinder")
        SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<User> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT u FROM  com.ninjabooks.domain.User u", User.class).stream();
    }

    @Override
    public Optional<User> getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        User user = currentSession.get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByName(String name) {
        return specifiedElementFinder.findSpecifiedElementInDB(name, DBColumnName.NAME);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return specifiedElementFinder.findSpecifiedElementInDB(email, DBColumnName.EMAIL);
    }

    @Override
    public void add(User user) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(user);
        currentSession.close();
    }

    @Override
    public void update(User user) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.update(user);
    }

    @Override
    public void delete(User user) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.delete(user);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

}
