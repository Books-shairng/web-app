package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
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
        long idBookToDelete = bookDao.getAll().findFirst().get().getId();

        bookDao.delete(idBookToDelete);

        assertThat(bookDao.getAll()).isEmpty();
    }

    @Test
    public void testTryDeleteBookWhichNotExistShouldThrowsException() throws Exception {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.delete(555L))
                .withNoCause();
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

    @Test
    public void testGetBookTitleWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByTitle("Effective Java");

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookAuthorWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByAuthor("J. Bloch");

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookISBNWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByAuthor("978-0321356680");

        assertThat(actual).isEmpty();
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book beforeUpdate = books.get(0);

        bookDao.add(beforeUpdate);
        beforeUpdate.setTitle("New Title");

        bookDao.update(beforeUpdate.getId());

        Book updatedBook =  bookDao.getAll().findFirst().get();

        assertThat(updatedBook.getTitle()).isEqualTo("New Title");
    }

    @Test
    public void testTryUpdateBookWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.update(555L))
            .withMessage("attempt to create saveOrUpdate event with null entity")
            .withNoCause();
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}
