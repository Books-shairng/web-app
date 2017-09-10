package com.ninjabooks.error.qrcode;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeUnableToCreateException extends QRCodeException
{
    public QRCodeUnableToCreateException(String message) {
        super(message);
    }

    public QRCodeUnableToCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
