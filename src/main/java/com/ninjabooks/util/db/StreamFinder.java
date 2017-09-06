package com.ninjabooks.util.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
@Transactional
public class StreamFinder implements SpecifiedElementFinder
{
    private final SessionFactory sessionFactory;

    @Autowired
    public StreamFinder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        Session currentSession = sessionFactory.openSession();
        Class classType = parameter.getClass();
        String hsqlQuery = "SELECT element FROM " + classType.getSimpleName() + " element WHERE " + columnName + "=:parameter";
        Query<T> query = currentSession.createQuery(hsqlQuery);
        query.setParameter("parameter", parameter);

        return (T) query.stream();
    }
}
