package com.ninjabooks.service;

import com.ninjabooks.domain.User;

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

   User createUser(User user);
}
