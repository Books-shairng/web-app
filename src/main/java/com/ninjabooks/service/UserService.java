package com.ninjabooks.service;

import com.ninjabooks.domain.User;

import java.security.Principal;

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

    /**
     * Retrun base information about user like
     * - id
     * - name
     * - email
     * @param email user email
     * @return found user
     */

   User baseUserInfo(Principal email);
}
