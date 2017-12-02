package com.ninjabooks.controller;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.utils.JSONDateConstans;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
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
@Sql(value = "classpath:history-scripts/it_std_hist_script.sql", executionPhase = BEFORE_TEST_METHOD)
public class HistoryControllerIT
{
    private static final String MINUS_DAYS = "10";
    private static final String EMPTY_HISTORY_MESSAGE = "User has no history";
    private static final int EXPECTED_SIZE = 1;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnHistroyWithExpectedSize() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[0].id").value(DomainTestConstants.ID))
            .andExpect(jsonPath("$.[0].returnDate").value(JSONDateConstans.RETURN_DATE))
            .andExpect(jsonPath("$.[0].['book'].id").value(DomainTestConstants.ID))
            .andExpect(jsonPath("$.[0].['book'].author").value(DomainTestConstants.AUTHOR))
            .andExpect(jsonPath("$.[0].['book'].isbn").value(DomainTestConstants.ISBN));
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID)
            .param("minusDays", MINUS_DAYS))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnMessageWhenUuserHasNoHistory() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID)
            .param("minusDays", MINUS_DAYS))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(EMPTY_HISTORY_MESSAGE));
    }
}