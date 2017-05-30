package com.ninjabooks.util;

import com.ninjabooks.domain.Book;
import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class CodeGenerator
{
    private final int DEFAULT_QR_CODE_LENGTH = 5;

    private char[] asciCoordinates;

    public CodeGenerator() {
        fillCoordinates();
    }

    //todo implent algorithm and write javadocs

    /**
     * Generate QR code data with following book. QR code length equals 5.
     *
     * @param book
     * @return
     */

    public String generateCode(Book book) {
        return "";
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
