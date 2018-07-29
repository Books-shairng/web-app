package com.ninjabooks.controller;

import com.ninjabooks.error.handler.QueryControllerHandler;
import com.ninjabooks.service.rest.database.DBQueryService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import org.hibernate.exception.SQLGrammarException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueryControllerTest extends BaseUTController
{
    private static final String WRONG_SQL_COMMAND = "DASDAS ADASDASDAS";
    private static final String SELECT_QUERY = "SELECT * FROM USER";
    private static final String URL = "/api/query";

    @Mock
    private DBQueryService dbQueryServiceMock;

    private QueryController sut;

    @Before
    public void setUp() throws Exception {
        sut = new QueryController(dbQueryServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new QueryControllerHandler()));
    }

    @Test
    public void testExecuteShouldReturnExpectedStatus() throws Exception {

        doPost(new HttpRequestBuilder(URL)
            .withParameter("q", SELECT_QUERY)
            .build());
}

    @Test
    public void testExecuteWithWrongSQLCommandShouldReturnBadRequestStatus() throws Exception {
        when(dbQueryServiceMock.execute(WRONG_SQL_COMMAND)).thenThrow(SQLGrammarException.class);

        doPost(new HttpRequestBuilder(URL)
            .withParameter("q", WRONG_SQL_COMMAND)
            .withStatus(BAD_REQUEST)
            .build());

        verify(dbQueryServiceMock, atLeastOnce()).execute(any());
    }
}
