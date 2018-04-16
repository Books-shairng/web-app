package com.ninjabooks.json.authentication;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationResponse implements Serializable
{
    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    @JsonCreator
    public AuthenticationResponse(@JsonProperty(value = "token") String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
