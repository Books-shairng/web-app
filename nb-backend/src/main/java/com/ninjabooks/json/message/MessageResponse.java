package com.ninjabooks.json.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class MessageResponse implements Serializable
{
    private static final long serialVersionUID = 4613055349722303651L;

    private final String message;

    @JsonCreator
    public MessageResponse(@JsonProperty(value = "messege") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
