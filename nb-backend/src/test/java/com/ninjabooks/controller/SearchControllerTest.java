package com.ninjabooks.controller;

import com.ninjabooks.service.rest.search.SearchService;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SearchControllerTest
{
    private final static String SEARCH_QUERY = DomainTestConstants.TITLE;
    private static final List SEARCH_RESULT = Collections.singletonList(DomainTestConstants.BOOK);
    private static final String MESSAGE_NOT_FOUND_QUERY = "Unfortunately search phrase not found";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SearchService searchServiceMock;

    private SearchController sut;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.sut = new SearchController(searchServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    public void testSearchBookWhenFoundMatchedBookShouldReturnStatusOK() throws Exception {
        when(searchServiceMock.search(anyString())).thenReturn(SEARCH_RESULT);

        mockMvc.perform(get("/api/search/")
            .param("query", SEARCH_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(searchServiceMock, atLeastOnce()).search(anyString());
    }

    @Test
    public void testSearchBookShouldReturnStatus204() throws Exception {
        when(searchServiceMock.search(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/search/")
            .param("query", SEARCH_QUERY))
            .andDo(print())
            .andExpect(jsonPath("$.message").value(MESSAGE_NOT_FOUND_QUERY));

        verify(searchServiceMock, atLeastOnce()).search(anyString());
    }
}
