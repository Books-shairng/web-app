package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.TransactionManager;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    private TransactionManager transactionManager;

    @Override
    public User login() {
        return null;
    }

    @Override
    public User logout() {
        return null;
    }

    @Override
    public User addUser(User user) {
        transactionManager = new TransactionManager(userDao.getCurrentSession());
        transactionManager.beginTransaction();
        try {
            userDao.add(user);
            transactionManager.commit();
        } catch (HibernateException e) {
            transactionManager.rollback();
        }

        return user;
    }
}
