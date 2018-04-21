package com.ninjabooks.service.dao.queue;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.Queue;

import static com.ninjabooks.util.constants.DomainTestConstants.ORDER_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.QUEUE_FULL;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class QueueServiceImplIT extends AbstractBaseIT
{
    private static final LocalDateTime CUSTOM_ORDER_DATE = LocalDateTime.now();

    @Autowired
    private QueueService sut;

    @Test
    public void testGetByOrderDateShouldReturnSteamWithExpectedQueue() throws Exception {
        Stream<Queue> actual = sut.getByOderDate(ORDER_DATE);

        assertThat(actual).extracting(queue -> queue).containsExactly(QUEUE_FULL);
    }

    @Test
    public void testGetByOrderDateShouldReturnEmptyStreamWhenQueueNotFound() throws Exception {
        Stream<Queue> actual = sut.getByOderDate(CUSTOM_ORDER_DATE);

        assertThat(actual).isEmpty();
    }
}
