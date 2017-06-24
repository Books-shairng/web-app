package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class DBBookDaoTest
{
    @Autowired
    private BookDao bookDao;

    private static final String AUTHOR = "C. Ho, R. Harrop, C. Schaefer";
    private static final String TITLE = "Pro Spring, 4th Edition";
    private static final String ISBN = "978-1430261513";

    private Book book;

    @Before

    public void setUp() throws Exception {
        this.book = new Book(TITLE, AUTHOR, ISBN);
    }

    @Test
    public void testAddBook() throws Exception {
        bookDao.add(book);

        Book actual = bookDao.getAll().findFirst().get();

        assertThat(actual.getId()).isEqualTo(book.getId());
    }

    @Test
    public void testDeleteBook() throws Exception {
        bookDao.add(book);
        bookDao.delete(book);
        assertThat(bookDao.getAll()).isEmpty();
    }

    @Test
    public void testTryDeleteBookWhichNotExistShouldThrowsException() throws Exception {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.delete(null))
                .withNoCause();
    }

    @Test
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        bookDao.add(book);

        Book actual = bookDao.getAll().findFirst().get();
        assertThat(actual.getId()).isEqualTo(book.getId());
    }
//
    @Test
    public void testGetBooksByTitle() throws Exception {
        bookDao.add(book);
        Stream<Book> actual = bookDao.getByTitle(TITLE);
        Book b = actual.findAny().get();
        assertThat(b.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        bookDao.add(book);
        Stream<Book> actual = bookDao.getByAuthor(AUTHOR);
        Book b = actual.findAny().get();
        assertThat(b.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void testGetBooksByISBN() throws Exception {
        bookDao.add(book);
        Stream<Book> actual = bookDao.getByISBN(ISBN);
        Book b = actual.findAny().get();
        assertThat(b.getIsbn()).isEqualTo(book.getIsbn());
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
        Stream<Book> actual = bookDao.getByISBN("978-0321356680");

        assertThat(actual).isEmpty();
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book beforeUpdate = book;
        bookDao.add(beforeUpdate);
        beforeUpdate.setTitle("New Title");

//        bookDao.getCurrentSession().flush();
        bookDao.update(beforeUpdate);
//        bookDao.getCurrentSession().getTransaction().commit();

        Book updatedBook =  bookDao.getById(book.getId());
//        bookDao.getCurrentSession().getTransaction().commit();

        assertThat(updatedBook.getTitle()).isEqualTo("New Title");
    }

    @Test
    public void testTryUpdateBookWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bookDao.update(null))
            .withMessage("attempt to create saveOrUpdate event with null entity")
            .withNoCause();
    }

}
