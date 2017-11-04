package com.ninjabooks.error.exception.qrcode;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeException extends Exception
{
    private static final long serialVersionUID = 401589992485992680L;

    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
