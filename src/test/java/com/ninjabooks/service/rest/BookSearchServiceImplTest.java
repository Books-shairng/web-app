package com.ninjabooks.service.rest;

import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.book.BookService;
import com.ninjabooks.service.rest.search.BookSearchServiceImpl;
import com.ninjabooks.service.rest.search.SearchService;
import com.ninjabooks.service.rest.search.SearchWrapper;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookSearchServiceImplTest
{
    private static final String SEARCH_QUERY = "Title";
    private static final Book BOOK = new Book();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookService bookServiceMock;

    @Mock
    private SearchWrapper searchWrapper;

    private SearchService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookSearchServiceImpl(bookServiceMock, searchWrapper);
        BOOK.setTitle(SEARCH_QUERY);
    }


    @Test
    public void testSearchBookShouldReturnFilledList() throws Exception {
        FullTextQuery fullTextQueryMock = prepareMocksDepedencies();
        when(fullTextQueryMock.getResultSize()).thenReturn(1);
        when(fullTextQueryMock.getResultList()).thenReturn(Collections.singletonList(BOOK));

        List<Book> actual = sut.search(SEARCH_QUERY);

        assertThat(actual).containsExactly(BOOK);
        verify(fullTextQueryMock, atLeastOnce()).getResultSize();
        verify(fullTextQueryMock, atLeastOnce()).getResultList();
    }

    @Test
    public void testSearchBookShoulRetturnEmptyListWhenNotFoundMatchedQueries() throws Exception {
        FullTextQuery fullTextQueryMock = prepareMocksDepedencies();
        when(fullTextQueryMock.getResultSize()).thenReturn(0);
        when(fullTextQueryMock.getResultList()).thenReturn(Collections.EMPTY_LIST);

        List<Book> actual = sut.search(SEARCH_QUERY);

        assertThat(actual).isEmpty();
        verify(fullTextQueryMock, atLeastOnce()).getResultSize();
        verify(fullTextQueryMock, atLeastOnce()).getResultList();
    }

    private FullTextQuery prepareMocksDepedencies() {
        FullTextSession fullTextSessionMock = mock(FullTextSession.class, Mockito.RETURNS_DEEP_STUBS);
        when(searchWrapper.search(any())).thenReturn(fullTextSessionMock);
        FullTextQuery fullTextQueryMock = mock(FullTextQuery.class);
        when(fullTextSessionMock.createFullTextQuery((Query) any(), any())).thenReturn(fullTextQueryMock);
        return fullTextQueryMock;
    }
}
