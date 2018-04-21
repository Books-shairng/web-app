package com.ninjabooks.service.rest.notification;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.CAN_EXTEND;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.ORDER_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class NotificationServiceImplIT extends AbstractBaseIT
{
    private static final int EXPECTED_SIZE = 1;
    private static final String UPDATE_BORROW_STATUS = "UPDATE BORROW SET ACTIVE = false WHERE ID = 1 ;";
    private static final String UPDATE_QUEUE_STATUS = "UPDATE QUEUE SET ACTIVE = false WHERE ID = 1 ;";

    @Autowired
    private NotificationService sut;

    @Test
    public void testFindUserBorrowsShouldReturnListOfBorrowsWithExpectedBookDto() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("bookDto.author", "bookDto.title", "bookDto.isbn", "bookDto.description")
            .containsExactly(tuple(AUTHOR, TITLE, ISBN, DESCRIPTION));
    }

    @Test
    public void testFindUserBorrowsShouldReturnListOfBorrowsWutgExpectedBorrowDto() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("borrowDto.borrowDate", "borrowDto.expectedReturnDate", "borrowDto.canExtendBorrow")
            .containsExactly(tuple(BORROW_DATE, EXPECTED_RETURN_DATE, CAN_EXTEND));
    }

    @Test
    @Sql(
        value = "classpath:sql_query/it_import.sql",
        statements = UPDATE_BORROW_STATUS,
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindUserBorrowsWhenUserNotHaveBorrowsShouldReturnEmptyList() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindUserBorrowsShouldReturnExpectedSizeOfList() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    public void testFindUserQueuesShouldReturnListOfQueuesWithExpectedBookDto() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("bookDto.author", "bookDto.title", "bookDto.isbn", "bookDto.description")
            .containsExactly(tuple(AUTHOR, TITLE, ISBN, DESCRIPTION));
    }

    @Test
    public void testFindUserQueuesShouldReturnListOfQueuesWithExpectedQueueDto() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("queueDto.orderDate")
            .containsExactly(ORDER_DATE);
    }

    @Test
    @Sql(
        value = "classpath:sql_query/it_import.sql",
        statements = UPDATE_QUEUE_STATUS,
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindUserQueuesWhenUserNotHaveQueuesShouldReturnEmptyList() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindUserQueuesShouldReturnExpectedSizeOfList() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }
}
