package com.ninjabooks.util.db;

import com.ninjabooks.domain.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class SpecifiedElementFinder
{
    private final SessionFactory sessionFactory;

    public SpecifiedElementFinder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Perform some specified operation in DB. For exmaple
     * repetive sql qeury which search almost similar data.
     *
     * @param <T> type which method should return
     * @param parameter specific parameter
     * @param columnName for example column name
     * @param enityType
     * @return any selected data collections
     */

    @SuppressWarnings("unchecked")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName, Class<? extends BaseEntity> enityType) {
        Session currentSession = sessionFactory.openSession();
        String hsqlQuery = "SELECT element FROM " + enityType.getSimpleName() + " element WHERE " + columnName + "=:parameter";
        Query<T> query = currentSession.createQuery(hsqlQuery);
        query.setParameter("parameter", parameter);

        return (T) query;
    }
}
