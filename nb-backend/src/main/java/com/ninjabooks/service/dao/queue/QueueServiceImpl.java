package com.ninjabooks.service.dao.queue;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.QueueDao;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class QueueServiceImpl extends GenericServiceImpl<Queue, Long> implements QueueService
{
    private final QueueDao queueDao;

    @Autowired
    public QueueServiceImpl(GenericDao<Queue, Long> genericDao, QueueDao queueDao) {
        super(genericDao);
        this.queueDao = queueDao;
    }

    @Override
    public Stream<Queue> getByOderDate(LocalDateTime orderDate) {
        return queueDao.getByOrderDate(orderDate);
    }
}
