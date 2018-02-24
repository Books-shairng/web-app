package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;

import static com.ninjabooks.util.constants.DomainTestConstants.BORROW;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBBorrowDaoIT extends AbstractBaseIT
{
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now();

    @Autowired
    private BorrowDao sut;

    @Test
    public void testAddBorrow() throws Exception {
        sut.add(BORROW);
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).containsExactly(BORROW);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBorrow() throws Exception {
        sut.delete(BORROW);
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).containsExactly(BORROW);
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<Borrow> actual = sut.getById(ID);

        assertThat(actual).contains(BORROW);
    }

    @Test
    public void testGetByIdEntityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Borrow> actual = sut.getById(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetReturnDate() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(EXPECTED_RETURN_DATE);

        assertThat(actual).containsExactly(BORROW);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBorrowDate() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(BORROW_DATE);

        assertThat(actual).containsExactly(BORROW);
    }


    @Test
    public void testGetReturnDateWhichNotExistShouldReturnsEmptyStream() throws Exception {
        Stream<Borrow> actual = sut.getByExpectedReturnDate(EXPECTED_RETURN_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBorrowDateWhichNotExistShouldReturnEmtpyStream() throws Exception {
        Stream<Borrow> actual = sut.getByBorrowDate(BORROW_DATE);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateBorrow() throws Exception {
        Borrow entityToUpdate = createFreshEnity();
        entityToUpdate.setBorrowDate(UPDATED_BORROW_DATE);

        sut.update(entityToUpdate);
        Stream<Borrow> actual = sut.getAll();

        Assertions.assertThat(actual).containsExactly(entityToUpdate);
    }

    private Borrow createFreshEnity() {
        Borrow updatedEnity = new Borrow(BORROW_DATE);
        updatedEnity.setId(ID);

        return updatedEnity;
    }

}
