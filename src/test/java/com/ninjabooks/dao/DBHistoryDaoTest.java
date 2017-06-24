package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.History;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBHistoryDaoTest
{
    @Autowired
    private HistoryDao historyDao;

    private static final String COMMENT = "Nice book";
    private static final LocalDate BORROW_DATE = LocalDate.now().minusDays(25);
    private static final LocalDate RETURN_DATE = LocalDate.now();

    private History history;

    @Before
    public void setUp() throws Exception {
        this.history  = new History(BORROW_DATE, RETURN_DATE);
    }


    @Test
    public void testAddHistory() throws Exception {
        historyDao.add(history);

        History actual = historyDao.getAll().findFirst().get();
        assertThat(actual.getId()).isEqualTo(history.getId());
    }

    @Test
    public void testDeleteHistory() throws Exception {
        historyDao.add(history);

        historyDao.delete(history);

        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteHistoryWhichNotExistShouldThorwsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.delete(null))
            .withNoCause();
    }

    @Test
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        historyDao.add(history);

        History actual = historyDao.getAll().findFirst().get();

        assertThat(actual.getId()).isEqualTo(history.getId());
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testUpdateHistory() throws Exception {
        History historyBeforeUpdate = history;
        historyDao.add(historyBeforeUpdate);

        historyBeforeUpdate.setComment("Nice");
        historyDao.update(historyBeforeUpdate);

        History historyAfterUpdate = historyDao.getAll().findFirst().get();

        assertThat(historyAfterUpdate.getComment()).isEqualTo("Nice");
    }

    @Test
    public void testUpdateHistoryNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.update(null))
            .withMessage("attempt to create saveOrUpdate event with null entity")
            .withNoCause();
    }

}
