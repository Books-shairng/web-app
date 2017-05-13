package com.ninjabooks.json.authentication;

import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationRequest implements Serializable
{
    private static final long serialVersionUID = -8445943548965154778L;

    private String email;
    private String password;

    public AuthenticationRequest() {
        super();
    }

    public AuthenticationRequest(String email, String password) {
        this.email= email;
        this.password = password;
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
