package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_STATUS;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
@Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class SearchControllerIT extends AbstractBaseIT
{
    private final static String SEARCH_QUERY = TITLE;
    private static final String BOOK_STATUS_AS_STRING = BOOK_STATUS.toString();
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
            .andExpect(jsonPath("$.searchResult.[0].id").value(ID))
            .andExpect(jsonPath("$.searchResult.[0].author").value(AUTHOR))
            .andExpect(jsonPath("$.searchResult.[0].title").value(TITLE))
            .andExpect(jsonPath("$.searchResult.[0].isbn").value(ISBN))
            .andExpect(jsonPath("$.searchResult.[0].description").value(DESCRIPTION))
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
