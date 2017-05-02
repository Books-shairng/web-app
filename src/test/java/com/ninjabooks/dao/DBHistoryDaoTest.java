package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.History;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBHistoryDaoTest
{
    @Autowired
    private HistoryDao historyDao;

    private List<History> histories;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        histories = createRecords();
        transactionManager = new TransactionManager(historyDao.getCurrentSession());
        transactionManager.beginTransaction();
    }

    private List<History> createRecords() {
        histories = new ArrayList<>();

        History firstHistory = new History();
        firstHistory.setComment("Nice book");
        firstHistory.setBorrowDate(LocalDate.now().minusDays(25));
        firstHistory.setReturnedDate(LocalDate.now());

        History secondHistory = new History();
        secondHistory.setComment("Awfull");
        secondHistory.setBorrowDate(LocalDate.now().minusDays(10));
        secondHistory.setReturnedDate(LocalDate.now());

        histories.add(firstHistory);
        histories.add(secondHistory);

        return histories;
    }

    @Test
    public void testAddHistory() throws Exception {
        historyDao.add(histories.get(0));

        assertThat(historyDao.getAll()).containsExactly(histories.get(0));
    }

    @Test
    public void testDeleteHistory() throws Exception {
        historyDao.add(histories.get(0));

        Long idToDelete = historyDao.getAll().findFirst().get().getId();
        historyDao.delete(idToDelete);

        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteHistoryWhichNotExistShouldThorwsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.delete(555L))
            .withNoCause();
    }

    @Test
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        histories.forEach(history -> historyDao.add(history));

        assertThat(historyDao.getAll()).containsExactly(histories.get(0), histories.get(1));
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(historyDao.getAll()).isEmpty();
    }

    @Test
    public void testUpdateHistory() throws Exception {
        History historyBeforeUpdate = histories.get(0);
        historyDao.add(historyBeforeUpdate);

        historyBeforeUpdate.setComment("Nice");
        historyDao.update(historyBeforeUpdate.getId());

        History historyAfterUpdate = historyDao.getAll().findFirst().get();

        assertThat(historyAfterUpdate.getComment()).isEqualTo("Nice");
    }

    @Test
    public void testUpdateHistoryNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> historyDao.update(666L))
            .withMessage("attempt to create saveOrUpdate event with null entity")
            .withNoCause();
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}
