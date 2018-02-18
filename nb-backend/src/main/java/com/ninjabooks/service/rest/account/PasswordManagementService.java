package com.ninjabooks.service.rest.account;

import com.ninjabooks.error.exception.management.ManagementException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface PasswordManagementService
{
    /**
     * Change user password
     *
     * @param id
     * @param password
     */

    void change(Long id, String password) throws ManagementException;
}
