package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.queue.QueueService;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Sql(value = "classpath:sql_query/rent-scripts/it_rent_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BookRentalServiceImplIT extends AbstractBaseIT
{
    private static final int EXPECTED_SIZE = 1;
    private static final String RANDOM_QR_DATA = "sjdasd29232";
    private static final String QUEUE_INSERT_QUERY =
        "INSERT INTO QUEUE (ID, ORDER_DATE, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-03-21 08:17:02', TRUE, 1, 1);";
    private static final String UPDATE_BOOK_BORROW_STATUS = "UPDATE BOOK SET STATUS = 'BORROWED' WHERE ID = 1;";

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private BookRentalService sut;

    @Test
    public void testRentBookShouldSucceedAndReturnExpectedSize() throws Exception {
        sut.rentBook(ID, DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    public void testRentBookShouldSucceedAndReturnBorrowStatus() throws Exception {
        sut.rentBook(ID, DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).extracting("book", Book.class)
            .extracting(Book::getStatus)
            .contains(BookStatus.BORROWED);
    }

    @Test
    public void testRentBookShouldSucceedAndReturnExpectedBook() throws Exception {
        sut.rentBook(ID, DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).extracting("book.title", "book.isbn", "book.author")
            .containsExactly(tuple(TITLE, ISBN, AUTHOR));
    }

    @Test
    public void testRentBookShouldSucceedAndReturnExpectedUser() throws Exception {
        sut.rentBook(ID, DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).extracting("user.name", "user.email")
            .containsExactly(tuple(NAME, EMAIL));
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookNotFound() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.rentBook(ID, RANDOM_QR_DATA))
            .withNoCause()
            .withMessageContaining("not found");
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/it_rent_import.sql",
         statements = QUEUE_INSERT_QUERY,
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRentBookShouldSetActiveToFalseInQueueTableAfterSucceedBorrow() throws Exception {
        sut.rentBook(ID, DATA);
        Stream<Queue> actual = queueService.getAll();

        assertThat(actual).extracting(BaseEntity::getIsActive).contains(false);
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/it_rent_import.sql",
         statements = UPDATE_BOOK_BORROW_STATUS,
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRentBookShouldThrowsExceptionWhenBookIsAlreadyBorrowed() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause()
            .withMessageContaining("already borrowed");
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/it_rent_queue_import.sql",
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRentBookShouldFailsWhenBookWasOrderedByOtherUser() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause()
            .withMessage("Unable to borrow book");
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/it_rent_queue_import.sql",
         statements = QUEUE_INSERT_QUERY,
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRentBookShouldFailsWhenUserOrderedBookAndNotOnFirstPosition() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause()
            .withMessage("Unable to borrow book");
    }

    @Test
    @Sql(value = {
        "classpath:sql_query/rent-scripts/it_rent_import.sql",
        "classpath:sql_query/rent-scripts/borrow_overflow_script.sql"
    },
         executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRentBookShouldThrowsExcpetionWhenLimitIsExceeded() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause()
            .withMessageContaining("exceeded the limit");
    }
}
