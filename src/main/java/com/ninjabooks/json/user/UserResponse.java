package com.ninjabooks.json.user;

import com.ninjabooks.security.SpringSecurityUser;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserResponse
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public UserResponse(SpringSecurityUser securityUser) {
        prepareResponse(securityUser);
    }

    public UserResponse() {
        super();
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
