package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DBBorrowDaoTest
{
    @Autowired
    private BorrowDao borrowDao;

    private List<Borrow> borrows;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        transactionManager = new TransactionManager(borrowDao.getCurrentSession());
        borrows = createRecords();
        transactionManager.beginTransaction();
    }

    private List<Borrow> createRecords() {
        Borrow firstBorrows = new Borrow();
        firstBorrows.setBorrowDate(LocalDate.of(2017, 1, 1));
        firstBorrows.getReturnDate();

        Borrow secondBorrow = new Borrow();
        secondBorrow.setBorrowDate(LocalDate.of(2016, 12, 12));

        List<Borrow> borrows= new ArrayList<>();
        borrows.add(firstBorrows);
        borrows.add(secondBorrow);

        return borrows;
    }

    @Test
    public void testAddBorrow() throws Exception {
        borrowDao.add(borrows.get(0));

        assertThat(borrowDao.getAll()).containsExactly(borrows.get(0));
    }

    @Test
    public void testDeleteBorrow() throws Exception {
        borrowDao.add(borrows.get(0));
        borrowDao.delete(3L);

        assertThat(borrowDao.getAll()).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        borrows.forEach(user -> borrowDao.add(user));

        assertThat(borrowDao.getAll()).containsExactly(borrows.get(0), borrows.get(1));
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}