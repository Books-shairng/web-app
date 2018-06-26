package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ENCRYPTED_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class QueryControllerIT extends BaseITController
{
    private static final String WRONG_SQL_COMMAND = "DASDAS ADASDASDAS";
    private static final String SELECT_QUERY = "SELECT * FROM USER";
    private static final String INSERT_QUERY =
        "INSERT INTO USER (NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) " +
        "VALUES ('John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)";
    public static final int EXPECTED_SIZE = 1;
    private static final String URL = "/api/query";

    @Test
    public void testExecuteShouldReturnExpectedStatus() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("q", SELECT_QUERY)
            .build());
    }

    @Test
    public void testExecuteWithSelectQueryShouldReturnExpectedMessage() throws Exception {
        Map<String, Object> res = ImmutableMap.of(
            "$..ID", ID.intValue(),
            "$..ACTIVE", true,
            "$..PASSWORD", ENCRYPTED_PASSWORD,
            "$..EMAIL", EMAIL,
            "$..NAME", NAME
        );

        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("q", SELECT_QUERY)
            .build(), res);
    }

    @Test
    public void testExecuteWithSelectQueryShouldRetunResponseWithExpectedSize() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("q", SELECT_QUERY)
            .build(), singletonMap("$.length()", EXPECTED_SIZE));
    }

    @Test
    public void testExecuteWithInsertQueryShouldReturnExpectedMessage() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("q", INSERT_QUERY)
            .build(), singletonMap("$..['EXECUTE RESULT']", ID.toString()));
    }

    @Test
    public void testExecuteWithWrongSQLCommandShouldThrowsException() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("q", WRONG_SQL_COMMAND)
            .withStatus(BAD_REQUEST)
            .build());
    }
}
