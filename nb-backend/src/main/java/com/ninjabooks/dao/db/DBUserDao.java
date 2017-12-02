package com.ninjabooks.dao.db;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.db.SpecifiedElementFinder;

import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DBUserDao implements UserDao
{
    private enum DBColumnName {NAME, EMAIL}

    private final SessionFactory sessionFactory;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBUserDao(SessionFactory sessionFactory,
                     @Qualifier(value = "queryFinder") SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<User> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT u FROM  com.ninjabooks.domain.User u", User.class).stream();
    }

    @Override
    public Optional<User> getById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = currentSession.get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByName(String name) {
        return specifiedElementFinder.findSpecifiedElementInDB(name, DBColumnName.NAME, User.class);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return specifiedElementFinder.findSpecifiedElementInDB(email, DBColumnName.EMAIL, User.class);
    }

    @Override
    public void add(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }

    @Override
    public void update(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
    }

    @Override
    public void delete(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(user);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
