package com.ninjabooks.service.dao.queue;

import com.ninjabooks.domain.Queue;
import com.ninjabooks.service.dao.generic.GenericService;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QueueService extends GenericService<Queue, Long>
{
    Stream<Queue> getByOderDate(LocalDateTime orderDate);
}
