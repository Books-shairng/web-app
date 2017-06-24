package com.ninjabooks.service;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.user.UserAlreadyExistException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface UserService
{
    /**
     * Create new user in database
     * @param user obtained by requested mapping
     * @return created user
     */

   User createUser(User user) throws UserAlreadyExistException;
}
