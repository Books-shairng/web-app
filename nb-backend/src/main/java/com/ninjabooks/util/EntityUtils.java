package com.ninjabooks.util;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.service.dao.generic.GenericService;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class EntityUtils
{
    /**
     * Generic utlis method which should return desried enity by id.
     *
     * @param service - dao service which calls own <code>getById(Long id)</code> method
     * @param id      - id of entity object should be returned
     * @return enity object
     */

    public static <E extends BaseEntity> E getEnity(GenericService<E, Long> service, Long id) {
        final String message = MessageFormat.format("Entity with id: {0} not found", id);
        return service.getById(id).orElseThrow(() -> new EntityNotFoundException(message));
    }

}
