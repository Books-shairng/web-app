package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.Borrow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBBorrowDaoIT
{
    private static final LocalDate BORROW_DATE = LocalDate.of(2017, 1, 1);
    private static final Borrow BORROW = new Borrow(BORROW_DATE);
    private static final LocalDate RETURN_DATE = BORROW.getReturnDate();

    @Autowired
    private BorrowDao borrowDao;


    @Test
    public void testAddBorrow() throws Exception {
        borrowDao.add(BORROW);
        Stream<Borrow> actual = borrowDao.getAll();

        assertThat(actual).containsExactly(BORROW);
    }

    @Test
    public void testDeleteBorrow() throws Exception {
        borrowDao.add(BORROW);
        borrowDao.delete(BORROW);
        Stream<Borrow> actual = borrowDao.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        borrowDao.add(BORROW);
        Stream<Borrow> actual = borrowDao.getAll();

        assertThat(actual).containsExactly(BORROW);
    }

    @Test
    public void testGetReturnDate() throws Exception {
        borrowDao.add(BORROW);
        Stream<Borrow> actual = borrowDao.getByReturnDate(RETURN_DATE);

//        assertThat(actual.getReturnDate()).isEqualTo(BORROW.getReturnDate());
    }

    @Test
    public void testGetBorrowDate() throws Exception {
        borrowDao.add(BORROW);
        Stream<Borrow> actual = borrowDao.getByBorrowDate(BORROW_DATE);

//        assertThat(actual.getReturnDate()).isEqualTo(BORROW.getReturnDate());
    }

}
