package com.ninjabooks.dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.Optional;
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

public interface GenericDao<E, K extends Serializable>
{
    /**
     * Return all mathching elements from T type table.
     *
     * @return stream with matching elements.
     */

    Stream<E> getAll();

    /**
     * Return matched element by id from T table.
     *
     * @param id to search
     * @return query with specified id
     */

    Optional<E> getById(K id);

    /**
     * Add entity type element to table.
     *
     * @param entity of object which will be added into system
     */

    void add(E entity);

    /**
     * Update entity type element in table with specified enity object.
     *
     * @param enity of object which will be edited
     */

    void update(E enity);


    /**
     * Delete entity type element in table with specified entity object.
     *
     * @param entity element which will be removed
     */

    void delete(E entity);

    /**
     * This method return current session status, which is necessary in
     * perform any operation on db.
     * By default it should implicate <code> SessionFactory.openSession </code>
     *
     * @return current connect session to db
     */

    Session getCurrentSession();
}
