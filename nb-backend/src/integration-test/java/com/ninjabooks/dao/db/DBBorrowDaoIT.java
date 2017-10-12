package com.ninjabooks.dao.db;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBBorrowDaoIT
{
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now();

    @Autowired
    private BorrowDao sut;

    @Test
    public void testAddBorrow() throws Exception {
        sut.add(DomainTestConstants.BORROW);
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBorrow() throws Exception {
        sut.delete(DomainTestConstants.BORROW);
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<Borrow> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.BORROW);
    }

    @Test
    public void testGetByIdEntityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Borrow> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetReturnDate() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBorrowDate() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(DomainTestConstants.BORROW_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
    }


    @Test
    public void testGetReturnDateWhichNotExistShouldReturnsEmptyStream() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBorrowDateWhichNotExistShouldReturnEmtpyStream() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(DomainTestConstants.BORROW_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateBorrow() throws Exception {
        Borrow entityToUpdate = createFreshEnity();
        entityToUpdate.setBorrowDate(UPDATED_BORROW_DATE);

        sut.update(entityToUpdate);
        Stream<Borrow> actual = sut.getAll();

        Assertions.assertThat(actual).containsExactly(entityToUpdate);
    }

    private Borrow createFreshEnity() {
        Borrow updatedEnity = new Borrow(DomainTestConstants.BORROW_DATE);
        updatedEnity.setId(DomainTestConstants.ID);

        return updatedEnity;
    }

}
