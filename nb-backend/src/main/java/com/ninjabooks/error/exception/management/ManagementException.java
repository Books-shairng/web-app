package com.ninjabooks.error.exception.management;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ManagementException extends Exception
{
    private static final long serialVersionUID = -8177303437553480547L;

    public ManagementException(String message) {
        super(message);
    }

    public ManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
