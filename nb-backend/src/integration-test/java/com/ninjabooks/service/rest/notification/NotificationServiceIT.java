package com.ninjabooks.service.rest.notification;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.CAN_EXTEND;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.ORDER_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:sql_query/it_import.sql")
public class NotificationServiceIT extends AbstractBaseIT
{
    private static final int EXPTECTED_SIZE = 1;
    private static final String SQL_UPDATE_BORROW_QUERY = "UPDATE BORROW SET ACTIVE=false WHERE ID=1";
    private static final String SQL_UPDATE_QUEUE_QUERY = "UPDATE QUEUE SET ACTIVE=false WHERE ID=1";

    @Autowired
    NotificationService sut;

    @Test
    public void testFindUserBorrowsShouldContainsExpectedBorrowsDto() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).extracting(
            "borrowDto.borrowDate",
            "borrowDto.expectedReturnDate",
            "borrowDto.canExtendBorrow")
            .contains(tuple(
                BORROW_DATE,
                EXPECTED_RETURN_DATE,
                CAN_EXTEND
            ));
    }

    @Test
    public void testFindUserBorrowsShouldContainsExpectedBookDto() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).extracting(
            "bookDto.author",
            "bookDto.title",
            "bookDto.isbn")
            .containsExactly(tuple(
                AUTHOR,
                TITLE,
                ISBN
            ));
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", statements = SQL_UPDATE_BORROW_QUERY)
    public void testFindUserBorrowsShouldReturnEmptyListWhenNotFoundAnyBook() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindUserBorrowsShouldContainsExpectedSize() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).hasSize(EXPTECTED_SIZE);
    }

    @Test
    public void testFindUserQueueShouldContainsExpectedBorrowsDto() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).extracting("queueDto.orderDate", LocalDateTime.class)
            .containsExactly(ORDER_DATE);
    }

    @Test
    public void testFindUserQueueShouldReturnExpectedPositionInQueue() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).extracting(QueueNotification::getPositionInQueue).containsExactly(EXPTECTED_SIZE);
    }

    @Test
    public void testFindUserQueueShouldContainsExpectedBookDto() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).extracting(
            "bookDto.author",
            "bookDto.title",
            "bookDto.isbn")
            .containsExactly(tuple(
                AUTHOR,
                TITLE,
                ISBN
            ));
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", statements = SQL_UPDATE_QUEUE_QUERY)
    public void testFindUserQueueShouldReturnEmptyListWhenNotFoundAnyBook() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindUserQuuesShouldContainsExpectedSize() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).hasSize(EXPTECTED_SIZE);
    }

}
