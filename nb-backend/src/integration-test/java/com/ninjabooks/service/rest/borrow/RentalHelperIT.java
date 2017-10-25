package com.ninjabooks.service.rest.borrow;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@Transactional
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RentalHelperIT
{
    private static final String RANDOM_QR_DATA = "idjre";

    @Autowired
    private RentalHelper sut;

    @Test
    public void testIsBookBorrowedShouldReturnFalseWhenBookIsFree() throws Exception {
        boolean actual = sut.isBookBorrowed(DomainTestConstants.BOOK);

        assertThat(actual).isFalse();
    }

    @Test
    public void testIsNotBelongToOtherUserQueueShouldReturnTrueWhenNotBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(DomainTestConstants.BOOK, DomainTestConstants.USER);

        assertThat(actual).isTrue();
    }

    @Test
    @Sql(value = "classpath:rent-scripts/it_rent_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testIsNotBelongToOtherUserQueueShouldReturnFalseWhenBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(DomainTestConstants.BOOK_FULL, DomainTestConstants.USER_FULL);

        assertThat(actual).isFalse();
    }

    @Test
    public void testFindBookByQRCodeShouldReturnFoundedBook() throws Exception {
        Book actual = sut.findBookByQRCode(DomainTestConstants.DATA);

        assertThat(actual)
            .extracting(Book::getTitle, Book::getAuthor, Book::getIsbn, Book::getQRCode)
            .contains(DomainTestConstants.TITLE, DomainTestConstants.AUTHOR, DomainTestConstants.ISBN,
                DomainTestConstants.QR_CODE);
}

    @Test
    public void testFindBookByQRCodeShouldThrowsExcpetionWhenBookNotFound() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.findBookByQRCode(RANDOM_QR_DATA))
            .withNoCause();
    }
}
