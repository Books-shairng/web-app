package com.ninjabooks.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * //todo Add some javadocs documentation
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class TransactionManager
{
    private final Session currentSession;

    public TransactionManager(final Session currentSession) {
        this.currentSession = currentSession;
    }

    /**
     * Start perform any transaction with database.
     */

    public void beginTransaction() {
//        if (!currentSession.getTransaction().isActive())
            currentSession.beginTransaction();
    }

    /**
     * Commit data to database
     * {@link Transaction#commit()}
     */

    public void commit() {
        currentSession.getTransaction().commit();
    }

    /**
     * Rollback current session with database, it means restore the previouus state
     * of current table (without saving data).
     * {@link Transaction#rollback()}
     */
    public void rollback() {
        currentSession.getTransaction().rollback();
    }

    /**
     * Flush currect session with database
     */

    public void flush() {
        currentSession.flush();
    }

    /**
     * Close current session
     */

    public void close() {
        currentSession.close();
    }
}
