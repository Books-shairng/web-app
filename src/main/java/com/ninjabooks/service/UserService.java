package com.ninjabooks.service;

import com.ninjabooks.domain.User;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface UserService
{
    User login();
    User logout();
    User addUser(User user);
}
