package com.ninjabooks.dao.db;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
interface SpecifiedElementFinder
{
    /**
     * Perform some specified operation in DB. For exmaple
     * repetive sql qeury which seatch almost similar data.
     *
     * @param parameter specific parameter
     * @param enumerator for example column name
     * @param <T> type which method should return
     * @return any selected data collections
     */

    <T, E> T findSpecifiedElementInDB(E parameter, Enum enumerator);
}
