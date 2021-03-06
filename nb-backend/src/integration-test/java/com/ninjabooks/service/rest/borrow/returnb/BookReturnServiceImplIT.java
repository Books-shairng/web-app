package com.ninjabooks.service.rest.borrow.returnb;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.History;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.history.HistoryService;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Sql(
    scripts = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BookReturnServiceImplIT extends AbstractBaseIT
{
    private static final LocalDate EXPECTED_DATE = LocalDate.now();
    private static final String RANDOM_QR_CODE_DATA = "adasjda123asd";
    private static final String UPDATE_BOOK_STATUS = "UPDATE BOOK SET STATUS = 'FREE' WHERE ID = 1;";

    @Autowired
    private BookDaoService bookDaoService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BookReturnService sut;

    @Test
    public void testReurnBookWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.returnBook(RANDOM_QR_CODE_DATA))
            .withNoCause()
            .withMessageContaining("not found");
    }

    @Test
    @Sql(
        value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
        statements = UPDATE_BOOK_STATUS,
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testReturnBookShouldThrowsExceptionWhenBookIsNotBorrowed() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.returnBook(DATA))
            .withNoCause()
            .withMessageContaining("not borrowed, unable to return");

    }

    @Test
    public void testReturnBookShouldChangeStatusBookStatusToFree() throws Exception {
        sut.returnBook(DATA);
        Stream<Book> actual = bookDaoService.getAll();

        assertThat(actual).extracting("status").containsExactly(BookStatus.FREE);
    }

    @Test
    public void testReturnBookShouldSetReturnDateToExpectedDate() throws Exception {
        sut.returnBook(DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).extracting("realReturnDate").containsExactly(EXPECTED_DATE);
    }

    @Test
    public void testReturnBookShouldSetActiveFieldToFalse() throws Exception {
        sut.returnBook(DATA);
        Stream<Borrow> actual = borrowService.getAll();

        assertThat(actual).extracting("isActive").containsExactly(false);
    }

    @Test
    public void testReturnBookShouldAddNewRecordToHistory() throws Exception {
        sut.returnBook(DATA);
        Stream<History> actual = historyService.getAll();

        assertThat(actual)
            .extracting("returnDate", "user.email", "book.title", "book.author")
            .containsExactly(tuple(EXPECTED_DATE, EMAIL, TITLE, AUTHOR));
    }
}
