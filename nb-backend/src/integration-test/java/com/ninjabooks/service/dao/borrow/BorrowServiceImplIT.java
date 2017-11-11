package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BorrowServiceImplIT
{
    private static final LocalDate CUSTOM_BORRROW_DATE = LocalDate.now();
    private static final LocalDate CUSTOM_EXPECTED_RETURN_DATE = LocalDate.now();

    @Autowired
    private BorrowService sut;

    @Test
    public void testGeyByBorrowDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(DomainTestConstants.BORROW_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(DomainTestConstants.BORROW);
    }

    @Test
    public void testGetByExpectedReturnDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(DomainTestConstants.BORROW);
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
