package com.ninjabooks.json.user;

import com.ninjabooks.security.user.SpringSecurityUser;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserResponse implements Serializable
{
    private static final long serialVersionUID = -3037009757401227758L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @JsonCreator
    public UserResponse(SpringSecurityUser securityUser) {
        prepareResponse(securityUser);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    private void prepareResponse(SpringSecurityUser springSecurityUser) {
        id = springSecurityUser.getId();
        email = springSecurityUser.getEmail();
        performNameSeparation(springSecurityUser.getName());
    }

    private void performNameSeparation(String name) {
        String[] separated = name.split(" ");
        firstName = separated[0];
        lastName = separated[1];
    }


}
