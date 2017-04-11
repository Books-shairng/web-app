package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DBBookDaoTest
{
    @Autowired
    private BookDao bookDao;

    private TransactionManager transactionManager;
    private List<Book> books;

    @Before
    public void setUp() throws Exception {
        transactionManager = new TransactionManager(bookDao.getCurrentSession());
        books = createRecords();
        transactionManager.beginTransaction();
    }

    private List<Book> createRecords() {
        Book effectiveJava = new Book();
        effectiveJava.setAuthor("J. Bloch");
        effectiveJava.setTitle("Effective Java");
        effectiveJava.setIsbn("978-0321356680");

        Book proSpring = new Book();
        proSpring.setAuthor("C. Ho, R. Harrop, C. Schaefer");
        proSpring.setTitle("Pro Spring, 4th Edition");
        proSpring.setIsbn("978-1430261513");

        List<Book> books = new ArrayList<>();
        books.add(effectiveJava);
        books.add(proSpring);

        return books;
    }

    @Test
    public void testAddBook() throws Exception {
        bookDao.add(books.get(0));

        assertThat(bookDao.getAll()).containsExactly(books.get(0));
    }

    @Test
    public void testDeleteBook() throws Exception {
        bookDao.add(books.get(0));
        bookDao.delete(8L);

        assertThat(bookDao.getAll()).isEmpty();
    }

    @Test
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        books.forEach(book -> bookDao.add(book));

        assertThat(bookDao.getAll()).containsExactly(books.get(0), books.get(1));
    }

    @Test
    public void testGetBooksByTitle() throws Exception {
        books.forEach(book -> bookDao.add(book));
        Stream<Book> actual = bookDao.getByTitle("Effective Java");

        assertThat(actual).containsExactly(books.get(0));
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        books.forEach(book -> bookDao.add(book));
        Stream<Book> actual = bookDao.getByAuthor("C. Ho, R. Harrop, C. Schaefer");

        assertThat(actual).containsExactly(books.get(1));
    }

    @Test
    public void testGetBooksByISBN() throws Exception {
        books.forEach(book -> bookDao.add(book));
        Stream<Book> actual = bookDao.getByISBN("978-1430261513");

        assertThat(actual).containsExactly(books.get(1));
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}