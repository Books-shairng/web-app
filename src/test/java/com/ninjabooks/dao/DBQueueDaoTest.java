package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQueueDaoTest
{
    @Autowired
    private QueueDao queueDao;

    private List<Queue> queues;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        transactionManager = new TransactionManager(queueDao.getCurrentSession());
        queues = createRecords();
        transactionManager.beginTransaction();
    }

    private List<Queue> createRecords() {
        Queue firstQueue = new Queue();
        firstQueue.setOrderDate(LocalDateTime.of(2017,  3, 21, 8, 17));

        Queue secondQueue = new Queue();
        secondQueue.setOrderDate(LocalDateTime.of(2015,  10, 8, 23, 59));


        List<Queue> queues= new ArrayList<>();
        queues.add(firstQueue);
        queues.add(secondQueue);

        return queues;
    }

    @Test
    public void testAddQeueu() throws Exception {
        queueDao.add(queues.get(0));

        assertThat(queueDao.getAll()).containsExactly(queues.get(0));
    }

    @Test
    public void testDeleteQeueu() throws Exception {
        queueDao.add(queues.get(0));
        queueDao.delete(3L);

        assertThat(queueDao.getAll()).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        queues.forEach(user -> queueDao.add(user));

        assertThat(queueDao.getAll()).containsExactly(queues.get(0), queues.get(1));
    }

    @Test
    public void testGetOrderByDate() throws Exception {
        queues.forEach(queue -> queueDao.add(queue));
        Queue actual = queueDao.getByOrderDate(queues.get(0).getOrderDate());

        assertThat(actual).isEqualTo(queues.get(0));
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}
