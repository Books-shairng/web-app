package com.ninjabooks.service.rest.search;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BookSearchServiceImplIT
{
    private static final String CUSTOM_SEARCH_QUERY = "Concurrency in practice";
    private static final String FULL_SEARCH_QUERY =
        DomainTestConstants.AUTHOR + " " + DomainTestConstants.ISBN + " " + DomainTestConstants.TITLE;

    @Autowired
    private SearchService sut;

    @Test
    public void testSearchByTitleShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(DomainTestConstants.TITLE);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testSearchByAuthorShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(DomainTestConstants.AUTHOR);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testSearchByISBNShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(DomainTestConstants.ISBN);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testSearchByFullQueryShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(FULL_SEARCH_QUERY);

        assertThat(actual).extracting(book -> book).containsExactly(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testSearchShouldReturnEmptyListWhenBookNotFound() throws Exception {
        List<Book> actual = sut.search(CUSTOM_SEARCH_QUERY);

        assertThat(actual).isEmpty();
    }
}
