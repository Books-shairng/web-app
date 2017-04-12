package com.ninjabooks.dao;

import com.ninjabooks.domain.Queue;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QueueDao extends GenericDao<Queue, Long>
{
    /**
     * Looking for book oder date.
     *
     * @param orderDate is parameter of desired order date.
     * @return book order date
     */

    Queue getByOrderDate(LocalDateTime orderDate);
}
