package com.ninjabooks.service.rest.account;

import com.ninjabooks.error.user.UserAlreadyExistException;
import com.ninjabooks.domain.User;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface AccountService
{
    /**
     * Create new user in database
     * @param user obtained by requested mapping
     * @return created user
     */

    void createUser(User user) throws UserAlreadyExistException;
}
