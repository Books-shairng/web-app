package com.ninjabooks.service.rest.search;

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
     * @return stream which contains books
     */

    <T> List<T> search(String query);
}
