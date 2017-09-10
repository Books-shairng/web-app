package com.ninjabooks.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class CodeGenerator
{
    private final int DEFAULT_QR_CODE_LENGTH = 10;

    private char[] asciCoordinates;

    public CodeGenerator() {
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
            if (asciCoordinates[index] == '\\')
                builder.append("\\");
        }

        return builder.toString();
    }

    /**
     * Fill ASCI coordinates from ! character (32) to ~ character (126)
     */

    private void fillCoordinates() {
        asciCoordinates = new char[93];
        for (int i = 0; i < asciCoordinates.length; i++)
            asciCoordinates[i] = (char) ('!' + i);
    }

}
