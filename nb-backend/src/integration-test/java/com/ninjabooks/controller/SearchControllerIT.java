package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class SearchControllerIT extends AbstractBaseIT
{
    private final static String SEARCH_QUERY = DomainTestConstants.TITLE;
    private static final String BOOK_STATUS_AS_STRING = DomainTestConstants.BOOK_STATUS.toString();
    private static final String RANDOM_SEARCH_QUERY = "Dkdkasoasd kskdkak dkaskdasd";
    private static final String MESSAGE_PHRASE_NOT_FOUND = "Unfortunately search phrase not found";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testSearchBookShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/search/")
            .param("query", SEARCH_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testSearchBookShouldReturnExpectedSearchResult() throws Exception {
        mockMvc.perform(get("/api/search/")
            .param("query", SEARCH_QUERY))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.searchResult.[0].id").value(DomainTestConstants.ID))
            .andExpect(jsonPath("$.searchResult.[0].author").value(DomainTestConstants.AUTHOR))
            .andExpect(jsonPath("$.searchResult.[0].title").value(DomainTestConstants.TITLE))
            .andExpect(jsonPath("$.searchResult.[0].isbn").value(DomainTestConstants.ISBN))
            .andExpect(jsonPath("$.searchResult.[0].description").value(DomainTestConstants.DESCRIPTION))
            .andExpect(jsonPath("$.searchResult.[0].status").value(BOOK_STATUS_AS_STRING));
    }

    @Test
    public void testSearchBookShouldReturnOkWhenPhraseNotFound() throws Exception {
        mockMvc.perform(get("/api/search/")
            .param("query", RANDOM_SEARCH_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testSearchBookShouldReturnExpectedMessageWhenPhraseNotFound() throws Exception {
        mockMvc.perform(get("/api/search/")
            .param("query", RANDOM_SEARCH_QUERY))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(MESSAGE_PHRASE_NOT_FOUND));
    }
}
