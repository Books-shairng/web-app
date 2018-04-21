package com.ninjabooks.service.rest.search;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.Book;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BookSearchServiceImplIT extends AbstractBaseIT
{
    private static final String CUSTOM_SEARCH_QUERY = "Concurrency in practice";
    private static final String FULL_SEARCH_QUERY = AUTHOR + " " + ISBN + " " + TITLE;

    @Autowired
    private SearchService sut;

    @Test
    public void testSearchByTitleShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(TITLE);

        assertThat(actual).extracting(book -> book).containsExactly(BOOK_FULL);
    }

    @Test
    public void testSearchByAuthorShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(AUTHOR);

        assertThat(actual).extracting(book -> book).containsExactly(BOOK_FULL);
    }

    @Test
    public void testSearchByISBNShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(ISBN);

        assertThat(actual).extracting(book -> book).containsExactly(BOOK_FULL);
    }

    @Test
    public void testSearchByFullQueryShouldReturnListOfBooks() throws Exception {
        List<Book> actual = sut.search(FULL_SEARCH_QUERY);

        assertThat(actual).extracting(book -> book).containsExactly(BOOK_FULL);
    }

    @Test
    public void testSearchShouldReturnEmptyListWhenBookNotFound() throws Exception {
        List<Book> actual = sut.search(CUSTOM_SEARCH_QUERY);

        assertThat(actual).isEmpty();
    }
}
