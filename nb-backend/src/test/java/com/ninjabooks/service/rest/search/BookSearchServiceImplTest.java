package com.ninjabooks.service.rest.search;

import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Collections;
import java.util.List;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookSearchServiceImplTest
{
    private static final String SEARCH_QUERY = "Title";
    private static final List<Book> LIST_WITH_BOOKS = Collections.singletonList(DomainTestConstants.BOOK);
    private static final List<Book> EMPTY_LIST = Collections.emptyList();
    private static final int EXPECTED_SIZE = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookServiceMock;

    @Mock
    private SearchWrapper searchWrapper;

    private SearchService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookSearchServiceImpl(bookServiceMock, searchWrapper);
    }

    @Test
    public void testSearchBookShouldReturnFilledList() throws Exception {
        FullTextQuery fullTextQueryMock = prepareMocksDepedencies();
        when(fullTextQueryMock.getResultSize()).thenReturn(EXPECTED_SIZE);
        when(fullTextQueryMock.getResultList()).thenReturn(LIST_WITH_BOOKS);

        List<Book> actual = sut.search(SEARCH_QUERY);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(fullTextQueryMock, atLeastOnce()).getResultSize();
        verify(fullTextQueryMock, atLeastOnce()).getResultList();
    }

    @Test
    public void testSearchBookShoulRetturnEmptyListWhenNotFoundMatchedQueries() throws Exception {
        FullTextQuery fullTextQueryMock = prepareMocksDepedencies();
        when(fullTextQueryMock.getResultSize()).thenReturn(0);
        when(fullTextQueryMock.getResultList()).thenReturn(EMPTY_LIST);

        List<Book> actual = sut.search(SEARCH_QUERY);

        assertThat(actual).isEmpty();
        verify(fullTextQueryMock, atLeastOnce()).getResultSize();
        verify(fullTextQueryMock, atLeastOnce()).getResultList();
    }

    private FullTextQuery prepareMocksDepedencies() {
        FullTextSession fullTextSessionMock = mock(FullTextSession.class, Mockito.RETURNS_DEEP_STUBS);
        when(searchWrapper.search(any())).thenReturn(fullTextSessionMock);
        FullTextQuery fullTextQueryMock = mock(FullTextQuery.class);
        when(fullTextSessionMock.createFullTextQuery(any(Query.class), any())).thenReturn(fullTextQueryMock);
        return fullTextQueryMock;
    }
}
