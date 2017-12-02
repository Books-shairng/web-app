package com.ninjabooks.service.rest.account;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.service.dao.user.UserService;

import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService
{
    private final static Logger logger = LogManager.getLogger(UserService.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User user) throws UserAlreadyExistException {
        String userName = user.getName();
        String email = user.getEmail();
        logger.info("Try add new user to database, email: {}, name: {}", email, userName);

        if (isUserAlreadyExist(user)) {
            String message = MessageFormat.format("Username email: {0} already exist in database", email);
            throw new UserAlreadyExistException(message);
        }

        User newUserToPersistent = prepareUserToPersist(user);
        userService.add(newUserToPersistent);

        logger.info("{} successfully added to database", userName);
    }

    private User prepareUserToPersist(User user) {
        return new User(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
    }

    /**
     * Check if user already exist in database
     *
     * @param user obtained by http request
     * @return true if presemt, false if not
     */

    private boolean isUserAlreadyExist(User user) {
        return userService.getByEmail(user.getEmail()).isPresent();
    }
}
