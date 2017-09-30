package com.ninjabooks.service.rest.order;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.error.order.OrderMaxLimitException;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql")
public class OrderBookServiceImplIT
{
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE QUEUE AND COMMIT;";
    private static final int EXPECTED_SIZE = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderBookService sut;

    @Test
    public void testOrderBookWhenUserHasBookShouldThrowsException() throws Exception {
        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();
    }

    @Test
    @Sql(value = "classpath:it_import.sql", statements = TRUNCATE_TABLE)
    public void testOrderBookShouldAddNewQueue() throws Exception {
        sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID);
        Optional<User> userOptional = userService.getById(DomainTestConstants.ID);
        User user = userOptional.orElseGet(userOptional::get);
        List<Queue> actual = user.getQueues();

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    @Sql(scripts = {"classpath:it_import.sql", "classpath:queue_overflow_script.sql"})
    public void testOrderBookShouldThrowsExceptionWhenQueueLimitExceed() throws Exception {
        assertThatExceptionOfType(OrderMaxLimitException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();
    }
}
