package com.ninjabooks.dao;

import com.ninjabooks.domain.Queue;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0.1
 */
public interface QueueDao extends GenericDao<Queue, Long>
{
    public Queue getByOrderDate(LocalDateTime orderDate);
}
