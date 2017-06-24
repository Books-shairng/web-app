package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Borrow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBBorrowDaoTest
{
    @Autowired
    private BorrowDao borrowDao;

    private static final LocalDate BORROW_DATE = LocalDate.of(2017, 1, 1);

    private Borrow borrow;

    @Before
    public void setUp() throws Exception {
        this.borrow = new Borrow(BORROW_DATE);
    }

    @Test
    public void testAddBorrow() throws Exception {
        borrowDao.add(borrow);

        Borrow actual = borrowDao.getAll().findFirst().get();

        assertThat(actual.getId()).isEqualTo(borrow.getId());
    }

    @Test
    public void testDeleteBorrow() throws Exception {
        borrowDao.add(borrow);
        borrowDao.delete(borrow);

        assertThat(borrowDao.getAll()).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        borrowDao.add(borrow);

        Borrow actual = borrowDao.getAll().findFirst().get();
        assertThat(actual.getBorrowDate()).isEqualTo(borrow.getBorrowDate());
    }

    @Test
    public void testGetReturnDate() throws Exception {
        borrowDao.add(borrow);
        LocalDate returnDate = borrow.getReturnDate();
        Borrow actual = borrowDao.getByReturnDate(returnDate);

        assertThat(actual.getReturnDate()).isEqualTo(borrow.getReturnDate());
    }

    @Test
    public void testGetBorrowDate() throws Exception {
        borrowDao.add(borrow);
        LocalDate returnDate = borrow.getBorrowDate();
        Borrow actual = borrowDao.getByReturnDate(returnDate);

        assertThat(actual.getReturnDate()).isEqualTo(borrow.getReturnDate());
    }

}
