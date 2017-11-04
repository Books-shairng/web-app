package com.ninjabooks.json.message;

import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class MessageResponse implements Serializable
{
    private static final long serialVersionUID = 4613055349722303651L;

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
