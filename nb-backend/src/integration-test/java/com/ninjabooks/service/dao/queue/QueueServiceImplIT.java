package com.ninjabooks.service.dao.queue;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class QueueServiceImplIT
{
    private static final LocalDateTime CUSTOM_ORDER_DATE = LocalDateTime.now();

    @Autowired
    private QueueService sut;

    @Test
    public void testGetByOrderDateShouldReturnSteamWithExpectedQueue() throws Exception {
        Stream<Queue> actual = sut.getByOderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).extracting(queue -> queue).containsExactly(DomainTestConstants.QUEUE_FULL);
    }

    @Test
    public void testGetByOrderDateShouldReturnEmptyStreamWhenQueueNotFound() throws Exception {
        Stream<Queue> actual = sut.getByOderDate(CUSTOM_ORDER_DATE);

        assertThat(actual).isEmpty();
    }
}
