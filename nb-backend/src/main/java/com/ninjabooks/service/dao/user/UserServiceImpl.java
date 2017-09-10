package com.ninjabooks.service.dao.user;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        this.userDao =  userDao;
    }

    @Override
    public Optional<User> getByName(String name) {
        return userDao.getByName(name);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    //    private final static Logger logger = LogManager.getLogger(UserService.class);
//
//    private final UserDao userDao;
//    private final PasswordEncoder passwordEncoder;
//
////    @Autowired
////    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
////        this.userDao = userDao;
////        this.passwordEncoder = passwordEncoder;
////    }
//
////    @Override
////    public User createUser(User user) throws UserAlreadyExistException {
////        logger.info("Try add new user to database, email:" + user.getEmail() + " , name:" + user.getName());
////
////        if (checkIfUserAlreadyExist(user)) {
////            logger.error(user.getEmail() + " already exist in database");
////            throw new UserAlreadyExistException("Username already exist in database");
////        }
////
////        User newUserToPersistent = new User(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
////        userDao.add(newUserToPersistent);
////        logger.info(user.getName() + " successfully added to database");
////        return newUserToPersistent;
////    }
//
//    @Override
//    public User findUserById(Long id) {
//        logger.info("Looking for user with id:{" + id + "}");
//        return userDao.getById(id);
//    }
//
////    /**
////     * Check if user already exist in database
////     *
////     * @param user obtained by http request
////     * @return true if exist
////     *         false if not
////     */
////
////    private boolean checkIfUserAlreadyExist(User user) {
////        return userDao.getByEmail(user.getEmail()) != null;
////    }
}
