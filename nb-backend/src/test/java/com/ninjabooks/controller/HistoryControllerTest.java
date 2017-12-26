package com.ninjabooks.controller;

import com.ninjabooks.service.rest.history.HistoryRestService;
import com.ninjabooks.util.constants.DomainTestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class HistoryControllerTest
{
    private static final String MINUS_DAYS = "10";
    private static final String EMPTY_HISTORY_MESSAGE = "User has no history";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private HistoryRestService historyRestServiceMock;

    private MockMvc mockMvc;
    private HistoryController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new HistoryController(historyRestServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnMessageWhenUuserHasNoHistory() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(EMPTY_HISTORY_MESSAGE));
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/history/{userID}/", DomainTestConstants.ID)
            .param("minusDays", MINUS_DAYS))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
