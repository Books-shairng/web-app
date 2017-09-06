package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class DBBookDaoIT
{
    private static final String AUTHOR = "C. Ho, R. Harrop, C. Schaefer";
    private static final String TITLE = "Pro Spring, 4th Edition";
    private static final String ISBN = "978-1430261513";
    private static final Book BOOK = new Book(TITLE, AUTHOR, ISBN);
    private static final String[] IGNORED_FIELDS = {"queues", "borrows", "histories"};
    private static final String UPDATED_TITLE = "New Title";

    @Autowired
    private BookDao bookDao;

    @Test
    public void testAddBook() throws Exception {
        bookDao.add(BOOK);
        Stream<Book> actual = bookDao.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(BOOK);
    }

    @Test
    public void testDeleteBook() throws Exception {
        bookDao.add(BOOK);
        bookDao.delete(BOOK);
        Stream<Book> actual = bookDao.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testTryDeleteBookWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.delete(null))
            .withNoCause();
    }

    @Test
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        bookDao.add(BOOK);
        Stream<Book> actual = bookDao.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).containsOnly(BOOK);
    }


    @Test
    public void testGetBooksByTitle() throws Exception {
        bookDao.add(BOOK);
        Stream<Book> actual = bookDao.getByTitle(TITLE);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(BOOK);
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        bookDao.add(BOOK);
        Stream<Book> actual = bookDao.getByAuthor(AUTHOR);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(BOOK);
    }

    @Test
    public void testGetBooksByISBN() throws Exception {
        bookDao.add(BOOK);
        Stream<Book> actual = bookDao.getByISBN(ISBN);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(BOOK);
    }

    @Test
    public void testGetBookTitleWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByTitle(TITLE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookAuthorWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByAuthor(AUTHOR);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookISBNWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = bookDao.getByISBN(ISBN);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testUpdateBookByEnity() throws Exception {
        Book beforeUpdate = BOOK;
        bookDao.add(beforeUpdate);
        beforeUpdate.setTitle(UPDATED_TITLE);

        bookDao.update(beforeUpdate);

        Book updatedBook = bookDao.getById(BOOK.getId());

        assertThat(updatedBook.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    public void testTryUpdateBookWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.update(null))
            .withMessage("attempt to create saveOrUpdate event with null entity")
            .withNoCause();
    }

}
