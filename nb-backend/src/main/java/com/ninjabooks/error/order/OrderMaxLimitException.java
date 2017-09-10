package com.ninjabooks.error.order;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderMaxLimitException extends OrderException
{

    public OrderMaxLimitException(String message) {
        super(message);
    }

    public OrderMaxLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
