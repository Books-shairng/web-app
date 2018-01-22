package com.ninjabooks.controller;

import com.ninjabooks.error.handler.QueryControllerHandler;
import com.ninjabooks.service.rest.database.DBQueryService;

import org.hibernate.exception.SQLGrammarException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueryControllerTest
{
    private static final String WRONG_SQL_COMMAND = "DASDAS ADASDASDAS";
    private static final String SELECT_QUERY = "SELECT * FROM USER";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private DBQueryService dbQueryServiceMock;

    private MockMvc mockMvc;
    private QueryController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QueryController(dbQueryServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new QueryControllerHandler())
            .build();
    }

    @Test
    public void testExecuteShouldReturnExpectedStatus() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", SELECT_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testExecuteWithWrongSQLCommandShouldReturnBadRequestStatus() throws Exception {
        when(dbQueryServiceMock.execute(any())).thenThrow(SQLGrammarException.class);

        mockMvc.perform(post("/api/query")
            .param("q", WRONG_SQL_COMMAND))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(dbQueryServiceMock, atLeastOnce()).execute(any());
    }
}
