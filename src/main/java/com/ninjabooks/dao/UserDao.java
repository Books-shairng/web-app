package com.ninjabooks.dao;

import com.ninjabooks.domain.User;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface UserDao extends GenericDao<User, Long>
{
    User getByName(String name);
    User getByEmail(String email);
}
