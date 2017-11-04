package com.ninjabooks.service.rest.order;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderBookServiceImplIT
{
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE QUEUE AND COMMIT;";
    private static final int EXPECTED_SIZE = 1;

    @Autowired
    private QueueService queueService;

    @Autowired
    private OrderBookService sut;

    @Test
    @Sql(value = "classpath:it_import.sql")
    public void testOrderBookWhenUserHasBookShouldThrowsException() throws Exception {
        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();
    }

    @Test
    @Rollback(false)
    @Transactional
    @Sql(value = "classpath:it_import.sql", statements = TRUNCATE_TABLE, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testOrderBookShouldAddNewQueue() throws Exception {
        sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID);
        Stream<Queue> actual = queueService.getAll();
        actual.forEach(this::assertEachElementOfStream);
    }

    private void assertEachElementOfStream(final Queue queue) {
        assertSoftly(softly -> {
            assertThat(queue.getOrderDate())
                .isBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L));
            assertThat(queue).extracting("user.name", "user.email")
                .contains(DomainTestConstants.NAME, DomainTestConstants.EMAIL);
            assertThat(queue).extracting("book.title", "book.author", "book.isbn")
                .contains(DomainTestConstants.TITLE, DomainTestConstants.AUTHOR, DomainTestConstants.ISBN);
        });

    }

    @Test
    @Sql(scripts = {"classpath:it_import.sql", "classpath:queue_overflow_script.sql"})
    public void testOrderBookShouldThrowsExceptionWhenQueueLimitExceed() throws Exception {
        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();
    }
}
