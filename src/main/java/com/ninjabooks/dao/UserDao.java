package com.ninjabooks.dao;

import com.ninjabooks.domain.User;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0.1
 */
public interface UserDao extends GenericDao<User, Long>
{
    public User getByName(String name);
    public User getByEmail(String email);
}
