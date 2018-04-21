package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.Borrow;

import static com.ninjabooks.util.constants.DomainTestConstants.BORROW;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BorrowServiceImplIT extends AbstractBaseIT
{
    private static final LocalDate CUSTOM_BORRROW_DATE = LocalDate.now();
    private static final LocalDate CUSTOM_EXPECTED_RETURN_DATE = LocalDate.now();

    @Autowired
    private BorrowService sut;

    @Test
    public void testGeyByBorrowDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(BORROW_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(BORROW);
    }

    @Test
    public void testGetByExpectedReturnDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(EXPECTED_RETURN_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(BORROW);
    }

    @Test
    public void testGetByBorrowDateShouldReturnEmptyStreamWhenBorrowNotFound() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(CUSTOM_BORRROW_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetByExpectedReturnDateShouldReturnEmptyStreamWhenBorrowNotFound() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(CUSTOM_EXPECTED_RETURN_DATE);

        assertThat(actual).isEmpty();
    }

}
