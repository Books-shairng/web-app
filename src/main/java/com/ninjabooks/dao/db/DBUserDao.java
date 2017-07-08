package com.ninjabooks.dao.db;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBUserDao implements UserDao, SpecifiedElementFinder
{
    private final static Logger logger = LogManager.getLogger(DBUserDao.class);

    private enum DBColumnName {NAME, EMAIL}

    private final SessionFactory sessionFactory;
    private final DBDaoHelper<User> daoHelper;

    @Autowired
    public DBUserDao(SessionFactory sessionFactory, DBDaoHelper<User> daoHelper) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
    }

    @Override
    public Stream<User> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT u FROM  com.ninjabooks.domain.User u", User.class).stream();
    }

    @Override
    public User getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        return currentSession.get(User.class, id);
    }

    @Override
    public User getByName(String name) {
        return findSpecifiedElementInDB(name, DBColumnName.NAME);
    }

    @Override
    public User getByEmail(String email) {
        return findSpecifiedElementInDB(email, DBColumnName.EMAIL);
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

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        Session currentSession = sessionFactory.openSession();
        String query = "select user from User user where " + columnName + "=:parameter";
        Query<User> userQuery = currentSession.createQuery(query, User.class);
        userQuery.setParameter("parameter", parameter);

        List<User> results = userQuery.getResultList();
        if (!results.isEmpty()) {
            return (T) results.get(0);
        }
        return null;
    }
}
