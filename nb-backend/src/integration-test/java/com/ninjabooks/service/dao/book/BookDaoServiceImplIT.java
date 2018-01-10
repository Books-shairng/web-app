package com.ninjabooks.service.dao.book;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.transaction.Transactional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class BookDaoServiceImplIT extends AbstractBaseIT
{
    private static final String CUSTOM_TITLE = "Java for dummies";
    private static final String CUSTOM_AUTHOR = "Barry A. Burd";
    private static final String CUSTOM_ISBN = "978-1-119-23555-2";

    @Autowired
    private BookDaoService sut;

    @Test
    public void testGetByTitleShouldRetrunStreamWithExpectedBook() throws Exception {
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testGetByAuthorShouldReturnStreamWithExpectedBook() throws Exception {
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testGetByISBNShouldReturnStreamWithExpectedBook() throws Exception {
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testGetByTitleShouldReturnEmptyStreamWhenEntityNotFound() throws Exception {
        Stream<Book> actual = sut.getByTitle(CUSTOM_TITLE);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetByAuthorShouldReturnEmptyStreamWhenEntityNotFound() throws Exception {
        Stream<Book> actual = sut.getByAuthor(CUSTOM_AUTHOR);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetByISBNShouldReturnEmptyStreamWhenEntityNotFound() throws Exception {
        Stream<Book> actual = sut.getByISBN(CUSTOM_ISBN);

        assertThat(actual).isEmpty();
    }
}
