package com.ninjabooks.error.exception.order;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderException extends Exception
{
    private static final long serialVersionUID = -3486218774360654866L;

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
