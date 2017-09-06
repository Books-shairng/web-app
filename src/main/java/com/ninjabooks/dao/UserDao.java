package com.ninjabooks.dao;

import com.ninjabooks.domain.User;

import java.util.Optional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface UserDao extends GenericDao<User, Long>
{
    /**
     * Searcb user by his name.
     *
     * @param name is parameter which is searched
     * @return user with desired name
     */

    Optional<User> getByName(String name);

    /**
     * Search user by his email
     *
     * @param email is parameter which is searched
     * @return user with desierd email
     */

    Optional<User> getByEmail(String email);
}
