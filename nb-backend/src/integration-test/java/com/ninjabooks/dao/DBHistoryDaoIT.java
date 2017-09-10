package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.History;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Ignore
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBHistoryDaoIT
{
    private static final String COMMENT = "Nice book";
    private static final LocalDate BORROW_DATE = LocalDate.of(2017, 1, 1);
    private static final LocalDate RETURN_DATE = BORROW_DATE.plusDays(30);
    private static final History HISTORY = new History(BORROW_DATE, RETURN_DATE);
    private static final Supplier<Stream<History>> HISTORY_STREAM_SUPPLIER = () -> Stream.of(HISTORY);
    private static final String UPDATED_COMMENT = "Nice";

    @Autowired
    private HistoryDao historyDao;

    @Test
    public void testAddHistory() throws Exception {
        historyDao.add(HISTORY);
        Stream<History> actual = historyDao.getAll();

        assertThat(actual).containsExactly(HISTORY);
    }

    @Test
    public void testDeleteHistoryByEnity() throws Exception {
        historyDao.add(HISTORY);
        historyDao.delete(HISTORY);

        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteHistoryWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.delete(null));
    }

    @Test
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        historyDao.add(HISTORY);
        historyDao.add(HISTORY);

        Stream<History> actual = historyDao.getAll();

        assertThat(actual).containsExactly(HISTORY, HISTORY);
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testUpdateHistoryByEntity() throws Exception {
        History historyBeforeUpdate = HISTORY;
        historyDao.add(historyBeforeUpdate);

        historyBeforeUpdate.setComment(UPDATED_COMMENT);
        historyDao.update(historyBeforeUpdate);

        Stream<History> actual = historyDao.getAll();

        assertThat(actual).containsExactly(historyBeforeUpdate);
    }

    @Test
    public void testUpdateHistoryWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.update(null));
    }

    @Test
    public void testAddCommentShouldUpdateQuery() throws Exception {
        historyDao.add(HISTORY);
        HISTORY.setComment(COMMENT);

        historyDao.update(HISTORY);

        History actual = historyDao.getAll().findFirst().get();
        assertThat(actual.getComment()).isEqualTo(COMMENT);
    }
}
