package com.ninjabooks.service.rest.borrow;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.Book;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.QR_CODE;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;
import static com.ninjabooks.util.constants.DomainTestConstants.USER_FULL;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RentalHelperIT extends AbstractBaseIT
{
    private static final String RANDOM_QR_DATA = "idjre";

    @Autowired
    private RentalHelper sut;

    @Test
    public void testIsBookBorrowedShouldReturnFalseWhenBookIsFree() throws Exception {
        boolean actual = sut.isBookBorrowed(BOOK);

        assertThat(actual).isFalse();
    }

    @Test
    public void testIsNotBelongToOtherUserQueueShouldReturnTrueWhenNotBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(BOOK, USER);

        assertThat(actual).isTrue();
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/it_rent_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testIsNotBelongToOtherUserQueueShouldReturnFalseWhenBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(BOOK_FULL, USER_FULL);

        assertThat(actual).isFalse();
    }

    @Test
    public void testFindBookByQRCodeShouldReturnFoundedBook() throws Exception {
        Book actual = sut.findBookByQRCode(DATA);

        assertThat(actual)
            .extracting(Book::getTitle, Book::getAuthor, Book::getIsbn, Book::getQRCode)
            .contains(TITLE, AUTHOR, ISBN,
                QR_CODE);
    }

    @Test
    public void testFindBookByQRCodeShouldThrowsExcpetionWhenBookNotFound() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.findBookByQRCode(RANDOM_QR_DATA))
            .withNoCause();
    }
}

