package com.ninjabooks.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class QRCodeGenerator
{
    private final int DEFAULT_QR_CODE_LENGTH = 10;

    private char[] asciCoordinates;

    public QRCodeGenerator() {
        fillCoordinates();
    }

    /**
     * Generate QR code data for new book. QR code length equals 5.
     *
     * @return random generated QR code.
     */

    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < DEFAULT_QR_CODE_LENGTH; i++) {
            int index = secureRandom.nextInt(asciCoordinates.length);
            builder.append(asciCoordinates[index]);
        }

        return builder.toString();
    }

    /**
     * Fill ASCI coordinates from ! character (32) to ~ character (126)
     */

    private void fillCoordinates() {
        asciCoordinates = ("qwertyuiopasdfghjkklzxccvbnm" +
                           "QWERTYUIOPASDFGHJKLZXCVBNM" +
                           "1234567890!@#$%^&*()-=_+~`" +
                           "{}[];':,.<>?|/").toCharArray();
    }
}
