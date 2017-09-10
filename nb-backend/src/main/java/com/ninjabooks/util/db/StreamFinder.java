package com.ninjabooks.util.db;

import com.ninjabooks.domain.BaseEntity;
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
public class StreamFinder extends SpecifiedElementFinder
{
    @Autowired
    public StreamFinder(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName, Class<? extends BaseEntity> enityType) {
        T query = super.findSpecifiedElementInDB(parameter, columnName, enityType);

        return (T) ((Query<T>)query).stream();
    }
}
