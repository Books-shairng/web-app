package com.ninjabooks.json.authentication;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationRequest implements Serializable
{
    private static final long serialVersionUID = -8445943548965154778L;

    @NotEmpty
    private final String email;

    @NotEmpty
    private final String password;

    @JsonCreator
    public AuthenticationRequest(@JsonProperty(value = "name") String email,
                                 @JsonProperty(value = "password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
