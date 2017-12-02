package com.ninjabooks.util.entity;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.service.dao.generic.GenericService;

import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class EntityUtilsWrapper
{
    public <E extends BaseEntity> E getEnity(Class<E> clazz, Long id) {
        return EntityUtils.getEnity(clazz, id);
    }

    public <E extends BaseEntity> E getEnity(GenericService<E, Long> genericService, Long id) {
        return EntityUtils.getEnity(genericService, id);
    }
}
