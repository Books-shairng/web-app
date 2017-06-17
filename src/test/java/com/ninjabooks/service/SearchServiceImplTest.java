package com.ninjabooks.service;

import com.ninjabooks.dao.db.DBBookDao;
import com.ninjabooks.dto.BookDto;
import org.hibernate.search.FullTextQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
//todo Rewrite this unit test
public class SearchServiceImplTest
{
    @Mock
    private DBBookDao bookDaoMock;

    @Mock
    private ModelMapper modelMapperMock;

    private SearchService searchServiceMock;

    private final static String SEARCH_QUERY = "Title";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.searchServiceMock = new SearchServiceImpl(bookDaoMock, modelMapperMock);
    }

    @Test
    public void testSearchBookShouldReturnFilledList() throws Exception {
        List<BookDto> bookDtos = Collections.singletonList(new BookDto());

        //given
        FullTextQuery fullTextQueryMock = Mockito.mock(FullTextQuery.class);
        when(fullTextQueryMock.list()).thenReturn(bookDtos);


        //when
        List<BookDto> actual =  fullTextQueryMock.list();

        //then
        verify(fullTextQueryMock, atLeastOnce()).list();

        assertThat(actual).isNotEmpty();

    }

    @Test
    public void testSearchBookShoulRetturnEmptyList_whenNotFoundMatchedQueries() throws Exception {
        List<BookDto> bookDtos = Collections.emptyList();

        //given
        FullTextQuery fullTextQueryMock = Mockito.mock(FullTextQuery.class);
        when(fullTextQueryMock.list()).thenReturn(bookDtos);


        //when
        List<BookDto> actual =  fullTextQueryMock.list();

        //then
        verify(fullTextQueryMock, atLeastOnce()).list();

        assertThat(actual).isEmpty();
    }
}
