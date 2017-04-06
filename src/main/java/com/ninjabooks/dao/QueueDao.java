package com.ninjabooks.dao;

import com.ninjabooks.domain.Queue;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QueueDao extends GenericDao<Queue, Long>
{
    Queue getByOrderDate(LocalDateTime orderDate);
}
