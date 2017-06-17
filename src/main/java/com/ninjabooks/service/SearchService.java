package com.ninjabooks.service;

import com.ninjabooks.dto.BookDto;

import java.util.List;

/**
 * This service perfom search on selcted tables.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface SearchService
{
    /**
     * Performs book search by giving any phrase.
     *
     * @param query - phrase with text to search
     * @return list which contains books transfered with DTO pattern {@link BookDto}
     */

    List<BookDto> searchBook(String query);
}
