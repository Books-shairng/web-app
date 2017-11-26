package com.ninjabooks.service.rest.history;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.json.history.GenericHistoryResponse;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:history-scripts/it_srd_hist_script.sql", executionPhase = BEFORE_TEST_METHOD)
public class UserHistoryRestServiceImplIT
{
    private static final long MINUS_NUMBER_OF_DAY = 10L;
    private static final long MINUS_ZERO_DAY = 0L;
    private static final int EXPECTED_SIZE = 1;
    private static final long RANDOM_USER_ID  = 6666L;
    private static final String TRUNCATE_HISTORY_TABLE = "TRUNCATE TABLE HISTORY ; ";

    @Autowired
    private HistoryRestService sut;

    @Test
    public void testGetHistoryShouldReturnListWithExpectedSize() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    public void testGetHistoryShouldReturnExpectedDtoFields() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual)
            .extracting("historyDto.returnDate", "bookDto.author", "bookDto.isbn", "bookDto.title")
            .containsExactly(tuple(
                DomainTestConstants.EXPECTED_RETURN_DATE,
                DomainTestConstants.AUTHOR,
                DomainTestConstants.ISBN,
                DomainTestConstants.TITLE));
    }

    @Test
    @Sql(
        value = "classpath:history-scripts/it_srd_hist_script.sql",
        statements = TRUNCATE_HISTORY_TABLE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testGetHistoryShouldReturnEmptyListWhenUserDontHaveAnyHistory() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenMinusDayIsLarge() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_NUMBER_OF_DAY, DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetHistoryShouldThrowsExceptionWhenUnableToFindUser() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.getHistory(MINUS_ZERO_DAY, RANDOM_USER_ID))
            .withNoCause();
    }
}
