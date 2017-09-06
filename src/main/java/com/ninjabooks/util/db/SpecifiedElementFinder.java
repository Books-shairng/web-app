package com.ninjabooks.util.db;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface SpecifiedElementFinder
{
    /**
     * Perform some specified operation in DB. For exmaple
     * repetive sql qeury which search almost similar data.
     *
     * @param <T> type which method should return
     * @param parameter specific parameter
     * @param enumerator for example column name
     * @return any selected data collections
     */

    <T, E> T findSpecifiedElementInDB(E parameter, Enum enumerator);
}
