package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderControllerIT extends BaseITController
{
    private static final String INSERT_USER = "INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES " +
        "(1, 'John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)";
    private static final String INSERT_BOOK = "INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, " +
        "DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'FREE', 'Some description')";
    private static final String ORDER_MESSAGE = "Book was corectly ordered";
    private static final String URL = "/api/order/{userID}/";

    @Test
    @Sql(statements = {INSERT_USER, INSERT_BOOK}, executionPhase = BEFORE_TEST_METHOD)
    public void testOrderBookShouldAddBookToUserQueue() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("bookID", String.valueOf(ID))
            .build(), singletonMap("$.message", ORDER_MESSAGE));
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql")
    public void testOrderBookShouldReturnErrorMessageWhenUserAlreadyOrderedBook() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("bookID", String.valueOf(ID))
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", format("User: {0} has already ordered this book", ID)));
    }

    @Test
    @Sql(
        scripts = {"classpath:sql_query/it_import.sql", "classpath:sql_query/queue_overflow_script.sql"},
        executionPhase = BEFORE_TEST_METHOD)
    public void testOrderBookShouldReturnErrorMessageWhenLimitExceed() throws Exception {
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("bookID", String.valueOf(ID))
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", format("User: {0} has exceeded the limit", ID)));
    }
}
