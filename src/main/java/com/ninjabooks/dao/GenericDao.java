package com.ninjabooks.dao;

import com.ninjabooks.util.TransactionManager;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * In computer software, a data access object (DAO) is an object that provides an
 * abstract interface to some type of database or other persistence mechanism.
 * By mapping application calls to the persistence layer, the DAO provides some
 * specific data operations without exposing details of the database. This
 * isolation supports the Single responsibility principle. It separates what data
 * access the application needs, in terms of domain-specific objects and data types
 * (the public interface of the DAO), from how these needs can be satisfied with a
 * specific DBMS, database schema, etc. (the implementation of the DAO).
 *
 * @see BookDao
 * @see UserDao
 * @see QRCodeDao
 * @see HistoryDao
 * @see BorrowDao
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

    T getById(K id);

    /**
     * Add entity type element to table.
     *
     * @param entity
     */

    void add(T entity);

    /**
     * Update entity type element in table with specified id.
     *
     * @param enity of element which will be edited
     */

    void update(T enity);

    /**
     * Delete entity type element in table with specified id.
     *
     * @param entity element which will be removed
     */

    void delete(T entity);

    /**
     * This method return current session status, which is necessary in
     * @see TransactionManager
     *
     * @return current connect session to db
     */

    Session getCurrentSession();
}
