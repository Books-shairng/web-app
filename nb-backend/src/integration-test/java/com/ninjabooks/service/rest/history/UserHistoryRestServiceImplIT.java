package com.ninjabooks.service.rest.history;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.json.history.GenericHistoryResponse;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/history-scripts/it_std_hist_script.sql", executionPhase = BEFORE_TEST_METHOD)
public class UserHistoryRestServiceImplIT extends AbstractBaseIT
{
    private static final long MINUS_NUMBER_OF_DAY = 10L;
    private static final long MINUS_ZERO_DAY = 0L;
    private static final int EXPECTED_SIZE = 1;
    private static final long RANDOM_USER_ID = 6666L;
    private static final String TRUNCATE_HISTORY_TABLE = "TRUNCATE TABLE HISTORY ; ";

    @Autowired
    private HistoryRestService sut;

    @Test
    public void testGetHistoryShouldReturnListWithExpectedSize() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    public void testGetHistoryShouldReturnExpectedDtoFields() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual)
            .extracting("historyDto.returnDate", "bookDto.author", "bookDto.isbn", "bookDto.title")
            .containsExactly(tuple(
                EXPECTED_RETURN_DATE,
                AUTHOR,
                ISBN,
                TITLE));
    }

    @Test
    @Sql(
        value = "classpath:sql_query/history-scripts/it_std_hist_script.sql",
        statements = TRUNCATE_HISTORY_TABLE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testGetHistoryShouldReturnEmptyListWhenUserDontHaveAnyHistory() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenMinusDayIsLarge() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_NUMBER_OF_DAY, ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetHistoryShouldThrowsExceptionWhenUnableToFindUser() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.getHistory(MINUS_ZERO_DAY, RANDOM_USER_ID))
            .withNoCause();
    }
}
