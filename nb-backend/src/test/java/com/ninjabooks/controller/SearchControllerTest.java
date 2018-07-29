package com.ninjabooks.controller;

import com.ninjabooks.service.rest.search.SearchService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SearchControllerTest extends BaseUTController
{
    private static final String URL = "/api/search/";
    private static final List<Object> SEARCH_RESULT = singletonList(BOOK);
    private static final String MESSAGE_NOT_FOUND_QUERY = "Unfortunately search phrase not found";

    @Mock
    private SearchService searchServiceMock;

    private SearchController sut;

    @Before
    public void setUp() throws Exception {
        sut = new SearchController(searchServiceMock);
        mockMvc(standaloneSetup(sut));
    }

    @Test
    public void testSearchBookWhenFoundMatchedBookShouldReturnStatusOK() throws Exception {
        when(searchServiceMock.search(anyString())).thenReturn(SEARCH_RESULT);

        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", TITLE)
            .build());

        verify(searchServiceMock, atLeastOnce()).search(anyString());
    }

    @Test
    public void testSearchBookShouldReturnStatus204() throws Exception {
        when(searchServiceMock.search(anyString())).thenReturn(emptyList());

        Map<String, Object> json = singletonMap("$.message", MESSAGE_NOT_FOUND_QUERY);
        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", TITLE)
            .build(), json);

        verify(searchServiceMock, atLeastOnce()).search(anyString());
    }
}
