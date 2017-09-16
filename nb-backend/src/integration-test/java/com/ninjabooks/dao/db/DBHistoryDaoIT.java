package com.ninjabooks.dao.db;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBHistoryDaoIT
{
    private static final String UPDATED_COMMENT = "Nice";

    @Autowired
    private HistoryDao sut;

    @Test
    public void testAddHistory() throws Exception {
        sut.add(DomainTestConstants.HISTORY);
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.HISTORY);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteHistoryByEnity() throws Exception {
        sut.delete(DomainTestConstants.HISTORY);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<History> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.HISTORY);
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<History> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.HISTORY);
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateHistoryByEntity() throws Exception {
        History enityToUpdate = createFreshEntity();
        enityToUpdate.setComment(UPDATED_COMMENT);

        sut.update(enityToUpdate);
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(enityToUpdate);
    }

    private History createFreshEntity() {
        History enityToUpdate = new History(DomainTestConstants.BORROW_DATE, DomainTestConstants.RETURN_DATE);
        enityToUpdate.setId(DomainTestConstants.ID);

        return enityToUpdate;
    }
}
