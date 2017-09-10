package com.ninjabooks.error.qrcode;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeNotFoundException extends QRCodeException
{
    public QRCodeNotFoundException(String message) {
        super(message);
    }

    public QRCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
