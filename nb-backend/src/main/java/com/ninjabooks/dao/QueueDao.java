package com.ninjabooks.dao;

import com.ninjabooks.domain.Queue;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QueueDao extends GenericDao<Queue, Long>
{
    /**
     * Looking for book order date.
     *
     * @param orderDate is parameter of desired order date.
     * @return book order date
     */

    Stream<Queue> getByOrderDate(LocalDateTime orderDate);
}
