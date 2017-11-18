package com.ninjabooks.util;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.service.dao.generic.GenericService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class EntityUtils
{
    private static SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        EntityUtils.sessionFactory = sessionFactory;
    }

    /**
     * Generic utlis method which should return desried enity by id.
     *
     * @param service - dao service which calls own <code>getById(Long id)</code> method
     * @param id      - id of entity object should be returned
     * @return enity object
     */

    public static <E extends BaseEntity> E getEnity(GenericService<E, Long> service, Long id) {
        return service.getById(id).orElseThrow(() -> new EntityNotFoundException(errorMessage(id)));
    }

    /**
     * Generic utlis method which should return desried enity by id.
     * Almost same functionality as
     * @see EntityUtils#getEnity(GenericService, Long)
     *
     * @param clazz   - class which represent entity in db method
     * @param id      - id of entity object should be returned
     * @return enity object
     */

    public static <E extends BaseEntity> E getEnity(Class<E> clazz, Long id) {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "select e from " + clazz.getSimpleName() + " e where id =:param";
        Query<E> query = session.createQuery(hqlQuery, clazz);
        query.setParameter("param", id);

        return query.uniqueResultOptional().orElseThrow(() -> new EntityNotFoundException(errorMessage(id)));
    }

    private static String errorMessage(Long id) {
        return MessageFormat.format("Entity with id: {0} not found", id);
    }

}
