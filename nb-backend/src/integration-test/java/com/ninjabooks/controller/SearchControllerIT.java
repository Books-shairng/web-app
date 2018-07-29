package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_STATUS;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static java.util.Collections.singletonMap;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class SearchControllerIT extends BaseITController
{
    private final static String SEARCH_QUERY = TITLE;
    private static final String BOOK_STATUS_AS_STRING = BOOK_STATUS.toString();
    private static final String RANDOM_SEARCH_QUERY = "Dkdkasoasd kskdkak dkaskdasd";
    private static final String MESSAGE_PHRASE_NOT_FOUND = "Unfortunately search phrase not found";
    private static final String URL = "/api/search/";

    @Test
    public void testSearchBookShouldSucceed() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", SEARCH_QUERY)
            .build());
    }

    @Test
    public void testSearchBookShouldReturnExpectedSearchResult() throws Exception {
        Map<String, Object> json = ImmutableMap.<String, Object>builder()
            .put("$.searchResult.[0].id", ID.intValue())
            .put("$.searchResult.[0].author", AUTHOR)
            .put("$.searchResult.[0].title", TITLE)
            .put("$.searchResult.[0].isbn", ISBN)
            .put("$.searchResult.[0].description", DESCRIPTION)
            .put("$.searchResult.[0].status", BOOK_STATUS_AS_STRING)
            .build();

        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", SEARCH_QUERY)
            .build(), json);
    }

    @Test
    public void testSearchBookShouldReturnOkWhenPhraseNotFound() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", RANDOM_SEARCH_QUERY)
            .build());
    }

    @Test
    public void testSearchBookShouldReturnExpectedMessageWhenPhraseNotFound() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withParameter("query", RANDOM_SEARCH_QUERY)
            .build(), singletonMap("$.message", MESSAGE_PHRASE_NOT_FOUND));
    }
}
