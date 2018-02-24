package com.ninjabooks.service.rest.order;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.service.dao.queue.QueueService;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderBookServiceImplIT extends AbstractBaseIT
{
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE QUEUE";

    @Autowired
    private QueueService queueService;

    @Autowired
    private OrderBookService sut;

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql")
    public void testOrderBookWhenUserHasBookShouldThrowsException() throws Exception {
        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(ID, ID))
            .withNoCause();
    }

    @Test
    @Rollback(false)
    @Transactional
    @Sql(value = "classpath:sql_query/it_import.sql",
         statements = TRUNCATE_TABLE,
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testOrderBookShouldAddNewQueue() throws Exception {
        sut.orderBook(ID, ID);
        Stream<Queue> actual = queueService.getAll();
        actual.forEach(this::assertEachElementOfStream);
    }

    private void assertEachElementOfStream(final Queue queue) {
        assertSoftly(softly -> {
            assertThat(queue.getOrderDate())
                .isBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L));
            assertThat(queue).extracting("user.name", "user.email")
                .contains(NAME, EMAIL);
            assertThat(queue).extracting("book.title", "book.author", "book.isbn")
                .contains(TITLE, AUTHOR, ISBN);
        });

    }

    @Test
    @Sql(scripts = {"classpath:sql_query/it_import.sql", "classpath:sql_query/queue_overflow_script.sql"})
    public void testOrderBookShouldThrowsExceptionWhenQueueLimitExceed() throws Exception {
        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(ID, ID))
            .withNoCause();
    }
}
