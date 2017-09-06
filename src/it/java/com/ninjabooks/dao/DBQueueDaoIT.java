package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.Queue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQueueDaoIT
{

    private static final LocalDateTime ORDER_DATE  = LocalDateTime.of(2017,  3, 21, 8, 17);
    private static final  Queue QUEUE = new Queue(ORDER_DATE);
    private static final LocalDateTime NEW_ORDER_DATE = LocalDateTime.now();

    @Autowired
    private QueueDao sut;

    @Test
    public void testAddQeueu() throws Exception {
        sut.add(QUEUE);
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(QUEUE);
    }

    @Test
    public void testDeleteQueue() throws Exception {
        sut.add(QUEUE);
        sut.delete(QUEUE);

        assertThat(sut.getAll()).isEmpty();
    }


    @Test
    public void testDeleteQueueWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> sut.delete(null))
            .withNoCause();
    }

    @Test
    public void testUpdateQueue() throws Exception {
        Queue beforeUpdate = QUEUE;
        sut.add(beforeUpdate);

        beforeUpdate.setOrderDate(NEW_ORDER_DATE);
        sut.update(beforeUpdate);

        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(beforeUpdate);
    }


    @Test
    public void testUpdateQueueWhichNotExistShouldThorwsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> sut.update(null))
            .withNoCause();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        sut.add(QUEUE);

        Stream<Queue> actual = sut.getAll();
        assertThat(actual).containsExactly(QUEUE);
    }

    @Test
    public void testGetOrderByDate() throws Exception {
        sut.add(QUEUE);
        Queue actual = sut.getByOrderDate(QUEUE.getOrderDate());

        assertThat(actual.getOrderDate()).isEqualTo(QUEUE.getOrderDate());
    }

}
