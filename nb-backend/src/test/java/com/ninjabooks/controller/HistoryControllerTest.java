package com.ninjabooks.controller;

import com.ninjabooks.service.rest.history.HistoryRestService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Collections.singletonMap;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class HistoryControllerTest extends BaseUTController
{
    private static final String MINUS_DAYS = "10";
    private static final String EMPTY_HISTORY_MESSAGE = "User has no history";
    private static final String URL = "/api/history/{userID}/";

    @Mock
    private HistoryRestService historyRestServiceMock;

    private HistoryController sut;

    @Before
    public void setUp() throws Exception {
        sut = new HistoryController(historyRestServiceMock);
        mockMvc(standaloneSetup(sut));
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnStatusOK() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build());
    }

    @Test
    public void testFetchUserHistoryWithoutParamsShouldReturnMessageWhenUuserHasNoHistory() throws Exception {
        Map<String, Object> json = singletonMap("$.message", EMPTY_HISTORY_MESSAGE);
        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build(), json);
    }

    @Test
    public void testFetchUserHistoryWithParamsShouldReturnStatusOK() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .withParameter("minusDays", MINUS_DAYS)
            .withUrlVars(ID)
            .build());
    }
}
