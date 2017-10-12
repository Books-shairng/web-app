package com.ninjabooks.service.dao.user;

import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.generic.GenericService;

import java.util.Optional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface UserService extends GenericService<User, Long>
{
    /**
     * Search user by his name.
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
