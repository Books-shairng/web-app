package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.UserAlreadyExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @version 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService
{
    private final static Logger logger = LogManager.getLogger(UserService.class);

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        logger.info("Try add new user to database, email:" + user.getEmail() + " , name:" + user.getName());

        if (checkIfUserAlreadyExist(user)) {
            logger.error(user.getEmail() + " already exist in database");
            throw new UserAlreadyExistException("Username already exist in database");
        }

        User newUserToPersistent = new User(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
        userDao.add(newUserToPersistent);
        logger.info(user.getName() + " successfully added to database");
        return newUserToPersistent;
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
