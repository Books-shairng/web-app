package com.ninjabooks.controller;

import com.ninjabooks.dto.BookDto;
import com.ninjabooks.service.rest.search.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SearchControllerTest
{
    private final static String SEARCH_QUERY = "Effective Java";

    @Mock
    private SearchService searchServiceMock;

    private SearchController sut;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.sut = new SearchController(searchServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    public void testSearchBookShouldFoundMatchedBookWithStatusOK() throws Exception {
        List<BookDto> resultList = Collections.singletonList(new BookDto());
//        when(searchServiceMock.search(SEARCH_QUERY)).thenReturn(resultList);

        mockMvc.perform(get("/api/search/{query}", SEARCH_QUERY))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(searchServiceMock, atLeastOnce()).search(anyString());
    }

    @Test
    public void testSearchBookShouldReturnStatus204() throws Exception {
        mockMvc.perform(get("/api/search/{query}", SEARCH_QUERY))
            .andExpect(status().isNoContent())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}
