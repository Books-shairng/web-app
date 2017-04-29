package com.ninjabooks.service;

import com.ninjabooks.error.UserAlreadyExistException;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

/**
 * @author Piotr 'pitrecki' Nowak
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService
{
    private final static Logger logger = LogManager.getLogger(UserService.class);

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    private TransactionManager transactionManager;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = false)
    public User createUser(User user) {
        logger.info("Try add new user to database, email:" + user.getEmail() + " , name:" + user.getName());
        User newUser = new User(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));

        if (checkIfUserAlreadyExist(user)) {
            logger.error(user.getEmail() + "already exist in database");
            throw new UserAlreadyExistException("Username already exist in database");
        }

        transactionManager =  new TransactionManager(userDao.getCurrentSession());
        transactionManager.beginTransaction();
        userDao.add(newUser);
        transactionManager.commit();
        logger.info(user.getName() + "successfully added to database");

        return newUser;
    }

    @Override
    public User baseUserInfo(Principal email) {
        transactionManager = new TransactionManager(userDao.getCurrentSession());
        User foundUser = null;

        try {
            logger.info("Try to found " + email + " in database and obtain information");
            transactionManager.beginTransaction();
            foundUser = userDao.getByEmail(email.getName());
            transactionManager.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
        }

        return foundUser;
    }

    /**
     * Check if user already exist in database
     *
     * @param user obtained by http request
     * @return true if exist
     *         false if not
     */

    private boolean checkIfUserAlreadyExist(User user) {
        return userDao.getByEmail(user.getEmail()) != null;
    }
}
