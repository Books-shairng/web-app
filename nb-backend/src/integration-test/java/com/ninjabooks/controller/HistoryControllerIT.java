package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.utils.JSONDateConstans.RETURN_DATE;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static java.util.Collections.singletonMap;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/history-scripts/it_std_hist_script.sql", executionPhase = BEFORE_TEST_METHOD)
public class HistoryControllerIT extends BaseITController
{
    private static final String MINUS_DAYS = "10";
    private static final String EMPTY_HISTORY_MESSAGE = "User has no history";
    private static final String URL = "/api/history/{userID}/";

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnStatusOK() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build());
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnHistroyWithExpectedSize() throws Exception {
        Map<String, Object> expectedJson = ImmutableMap.of(
            "$.[0].id", ID.intValue(),
            "$.[0].returnDate", RETURN_DATE.value(),
            "$.[0].['book'].id", ID.intValue(),
            "$.[0].['book'].author", AUTHOR,
            "$.[0].['book'].isbn", ISBN
        );

        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build(), expectedJson);
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnStatusOK() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("minusDays", MINUS_DAYS)
            .build());
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnMessageWhenUuserHasNoHistory() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("minusDays", MINUS_DAYS)
            .build(), singletonMap("$.message", EMPTY_HISTORY_MESSAGE));
    }
}
