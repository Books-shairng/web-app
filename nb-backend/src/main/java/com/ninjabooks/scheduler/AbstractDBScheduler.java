package com.ninjabooks.scheduler;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.service.dao.generic.GenericService;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class AbstractDBScheduler
{
    protected abstract <E extends BaseEntity> void executeTask(GenericService<E, Long> genericService);
}
