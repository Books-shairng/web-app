package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class represent user in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "Users")
public class User extends BaseEntity
{
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public User() {
    }

    /**
     * Create new instance of user object
     *
     * @param name
     * @param email
     * @param password
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
