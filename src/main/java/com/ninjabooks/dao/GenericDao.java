package com.ninjabooks.dao;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * This interface reperesent generic dao, for more information
 * look in google
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface GenericDao<T, K extends Serializable>
{
    /**
     * Return all mathching elements from T type table.
     *
     * @return stream with matching elements.
     */

    Stream<T> getAll();

    /**
     * Return matched element by id from T table.
     *
     * @param id to search
     * @return query with specified id
     */

    T getById(Long id);

    /**
     * Add t type element to table.
     *
     * @param t
     */

    void add(T t);

    /**
     * Update t type element in table with specified id.
     *
     * @param id of element which will be edited
     */

    void update(K id);

    /**
     * Delete t type element in table with specified id.
     *
     * @param id of element which will be removed
     */

    void delete(K id);
}
