package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBBookDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.utils.TestConfig;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)

public class DBBookDaoTest
{
    @Autowired
    private DBBookDao dbBookDao;

    @Test
    @Transactional
    public void testDEMO() throws Exception {
        Book book = new Book("Effective Java", "J. Bloch", "1111111");
        dbBookDao.add(book);

        Assertions.assertThat(dbBookDao).isSameAs(book);
    }
}