package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Queue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQueueDaoTest
{
    @Autowired
    private QueueDao queueDao;

    private final static LocalDateTime ORDER_DATE  = LocalDateTime.of(2017,  3, 21, 8, 17);

    private Queue queue;

    @Before
    public void setUp() throws Exception {
        this.queue = new Queue(ORDER_DATE);
    }



    @Test
    public void testAddQeueu() throws Exception {
        queueDao.add(queue);
        Queue actual = queueDao.getAll().findFirst().get();
        assertThat(actual.getOrderDate()).isEqualTo(queue.getOrderDate());
    }

    @Test
    public void testDeleteQueue() throws Exception {
        queueDao.add(queue);
        queueDao.delete(queue);

        assertThat(queueDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteQueueNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> queueDao.delete(null))
            .withNoCause();
    }

    @Test
    public void testUpdateQueue() throws Exception {
        Queue beforeUpdate = queue;

        LocalDateTime newOrderDate = LocalDateTime.now();
        beforeUpdate.setOrderDate(newOrderDate);
        queueDao.update(beforeUpdate);

        Queue afterUpdate = queueDao.getAll().findFirst().get();

        assertThat(afterUpdate.getOrderDate()).isEqualTo(newOrderDate);
    }

    @Test
    public void testUpdateQueueNotExistShouldThorwsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> queueDao.update(null))
            .withNoCause();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        queueDao.add(queue);

        Queue actual = queueDao.getAll().findFirst().get();
        assertThat(actual.getOrderDate()).isEqualTo(queue.getOrderDate());
    }

    @Test
    public void testGetOrderByDate() throws Exception {
        queueDao.add(queue);
        Queue actual = queueDao.getByOrderDate(queue.getOrderDate());

        assertThat(actual.getOrderDate()).isEqualTo(queue.getOrderDate());
    }

    @Test
    public void testGetOrderByDateWhichNotExistShouldReturnNull() throws Exception {
        Queue actual = queueDao.getByOrderDate(LocalDateTime.now());
        assertThat(actual).isNull();
    }

}
