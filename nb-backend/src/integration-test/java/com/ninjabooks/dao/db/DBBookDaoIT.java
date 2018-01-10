package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.BookDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBBookDaoIT extends AbstractBaseIT
{
    private static final String[] IGNORED_FIELDS = {"queues", "borrows", "histories", "comments"};
    private static final String UPDATED_TITLE = "New Title";

    @Autowired
    private BookDao sut;

    @Test
    public void testAddBook() throws Exception {
        sut.add(DomainTestConstants.BOOK);
        Stream<Book> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(DomainTestConstants.BOOK);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBook() throws Exception {
        sut.delete(DomainTestConstants.BOOK);
        Stream<Book> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetByID() throws Exception {
        Optional<Book> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).hasValueSatisfying(book -> {
            assertThat(book.getAuthor()).isEqualTo(DomainTestConstants.AUTHOR);
            assertThat(book.getTitle()).isEqualTo(DomainTestConstants.TITLE);
            assertThat(book.getIsbn()).isEqualTo(DomainTestConstants.ISBN);
        });
    }

    @Test
    public void testGetByIdBookWhichNotExistShouldRetunEmptyOptional() throws Exception {
        Optional<Book> actual = sut.getById(DomainTestConstants.ID);

        AssertionsForClassTypes.assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        Stream<Book> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).containsOnly(DomainTestConstants.BOOK);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBooksByTitle() throws Exception {
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(DomainTestConstants.BOOK);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBooksByAuthor() throws Exception {
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(DomainTestConstants.BOOK);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBooksByISBN() throws Exception {
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).contains(DomainTestConstants.BOOK);
    }

    @Test
    public void testGetBookTitleWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookAuthorWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBookISBNWhichNotExistShouldBeEmpty() throws Exception {
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateBookByEnity() throws Exception {
        Book enityToUpdate = createFreshEnity();
        enityToUpdate.setTitle(UPDATED_TITLE);

        sut.update(enityToUpdate);
        Stream<Book> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FIELDS).containsExactly(enityToUpdate);
    }

    private Book createFreshEnity() {
        Book enityToUpdate = new Book(DomainTestConstants.TITLE, DomainTestConstants.AUTHOR, DomainTestConstants.ISBN);
        enityToUpdate.setId(DomainTestConstants.ID);

        return enityToUpdate;
    }
}
