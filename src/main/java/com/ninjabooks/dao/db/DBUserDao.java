package com.ninjabooks.dao.db;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
public class DBUserDao implements UserDao, SpecifiedElementFinder
{
    private final static Logger logger = LogManager.getLogger(DBUserDao.class);

    private enum DBColumnName {NAME, EMAIL}

    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBUserDao(SessionFactory sessionFactory) {
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
    public Stream<User> getAll() {
        return currentSession.createQuery("SELECT u FROM  com.ninjabooks.domain.User u", User.class).stream();
    }

    @Override
    public User getById(Long id) {
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
        currentSession.save(user);
    }

    @Override
    public void update(Long id) {
        currentSession.update(id);
    }

    @Override
    public void delete(Long id) {
        User user = currentSession.get(User.class, id);
        currentSession.delete(user);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        String query = "select user from User user where " + columnName + "=:parameter";
        Query<User> bookQuery = currentSession.createQuery(query, User.class);
        bookQuery.setParameter("parameter", parameter);

        return (T) bookQuery.getSingleResult();

    }
}
