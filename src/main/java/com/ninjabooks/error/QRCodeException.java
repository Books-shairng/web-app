package com.ninjabooks.error;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeException extends Exception
{
    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
