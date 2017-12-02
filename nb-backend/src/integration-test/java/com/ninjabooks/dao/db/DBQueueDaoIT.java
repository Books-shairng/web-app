package com.ninjabooks.dao.db;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.QueueDao;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBQueueDaoIT
{
    private static final LocalDateTime NEW_ORDER_DATE = LocalDateTime.now();

    @Autowired
    private QueueDao sut;

    @Test
    public void testAddQueue() throws Exception {
        sut.add(DomainTestConstants.QUEUE);
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteQueue() throws Exception {
        sut.delete(DomainTestConstants.QUEUE);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
    }

    @Test
    public void testGetAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<Queue> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.QUEUE);
    }

    @Test
    public void testGetByIdWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Queue> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetOrderByDate() throws Exception {
        Stream<Queue> actual = sut.getByOrderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
    }

    @Test
    public void testGetOrderByDateWhichNotExistShouldReturnEmptyStream() throws Exception {
        Stream<Queue> actual = sut.getByOrderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateQueue() throws Exception {
        Queue entityToUpdate = creafreFreshEntity();
        entityToUpdate.setOrderDate(NEW_ORDER_DATE);

        sut.update(entityToUpdate);
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(entityToUpdate);
    }

    private Queue creafreFreshEntity() {
        Queue entityToUpdate = new Queue(DomainTestConstants.ORDER_DATE);
        entityToUpdate.setId(DomainTestConstants.ID);

        return entityToUpdate;
    }

}
