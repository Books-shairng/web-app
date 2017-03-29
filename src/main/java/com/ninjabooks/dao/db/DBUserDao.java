package com.ninjabooks.dao.db;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.User;
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
public class DBUserDao implements GenericDao<User, Long>
{
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            currentSession = sessionFactory.openSession();
        }
//        this.currentSession = sessionFactory.getCurrentSession();
    }

    @Override
    public Stream<User> getAll() {
        return currentSession.createQuery("SELECT u FROM  User u", User.class).stream();
    }

    @Override
    public User getById(Long id) {
        return currentSession.get(User.class, id);
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
        currentSession.delete(id);
    }
}
