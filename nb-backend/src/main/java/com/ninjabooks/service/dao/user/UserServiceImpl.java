package com.ninjabooks.service.dao.user;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @version 1.0
 */
@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService
{
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(GenericDao<User, Long> genericDao, UserDao userDao) {
        super(genericDao);
        this.userDao = userDao;
    }

    @Override
    public Optional<User> getByName(String name) {
        return userDao.getByName(name);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

}
