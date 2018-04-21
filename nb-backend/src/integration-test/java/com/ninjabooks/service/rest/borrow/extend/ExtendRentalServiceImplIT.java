package com.ninjabooks.service.rest.borrow.extend;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.error.exception.borrow.BorrowException;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class ExtendRentalServiceImplIT extends AbstractBaseIT
{
    private static final LocalDate ACTUAL_DATE_MOVE_BY_TWO_WEEKS = LocalDate.now().plusWeeks(2);
    private static final LocalDate ACTUAL_DATE_MOVE_BY_THREE_WEEKS = LocalDate.now().plusWeeks(3);
    private static final String TRUNCATE_BOOK_TABLE = "TRUNCATE TABLE BOOK ;";
    private static final String TRUNCATE_BORROW_TABLE = "TRUNCATE TABLE BORROW ;";
    private static final String UPDATE_EXTEND_STATUS = "UPDATE BORROW SET CAN_EXTEND_RETURN_DATE=FALSE WHERE ID=1 ;";

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private ExtendRentalService sut;

    @Test
    public void testExtendReturnDateShouldReturnExpectedStatus() throws Exception {
        sut.extendReturnDate(ID, ID);
        Stream<Borrow> actual = borrowDao.getAll();

        assertThat(actual).extracting(Borrow::getCanExtendBorrow).containsExactly(false);
    }

    @Test
    public void testExtendReturnDateShouldExtendReturnDateByTwoWeeks() throws Exception {
        sut.extendReturnDate(ID, ID);
        Borrow actual = borrowDao.getById(ID).get();

        assertThat(actual.getExpectedReturnDate())
            .isBetween(ACTUAL_DATE_MOVE_BY_TWO_WEEKS, ACTUAL_DATE_MOVE_BY_THREE_WEEKS);
    }

    @Test
    @Sql(
        value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
        statements = {TRUNCATE_BORROW_TABLE, TRUNCATE_BOOK_TABLE},
        executionPhase = BEFORE_TEST_METHOD)
    public void testExtendReturnDateShouldThrowsExceptionWhenBookNotExist() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.extendReturnDate(ID, ID))
            .withNoCause();
    }

    @Test
    @Sql(
        statements = TRUNCATE_BORROW_TABLE,
        value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
        executionPhase = BEFORE_TEST_METHOD)
    public void testEtendReturnDateShouldThrowsExceptionWhenBookIsNotBorrowed() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.extendReturnDate(ID, ID))
            .withNoCause()
            .withMessageContaining("is not borrowed");
    }

    @Test
    @Sql(
        value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
        statements = UPDATE_EXTEND_STATUS,
        executionPhase = BEFORE_TEST_METHOD)
    public void testEtendReturnDateShouldThrowsExceptionWhenExtendStatusIsFalse() throws Exception {
        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.extendReturnDate(ID, ID))
            .withNoCause();
    }

}
