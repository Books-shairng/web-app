package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_CONTENT;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.utils.JSONDateConstans.COMMENT_DATE;

import java.util.Collections;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentControllerIT extends BaseITController
{
    private static final String NO_COMMENTS_MESSAGE = "Book does not contains any comments";
    private static final int EXPECTED_SIZE = 1;
    private static final int COMMENT_DEFAULT_LENGTH = 250;
    private static final String JSON_REQUEST_WITH_COMMENT =
        "{\"comment\" : \"" + COMMENT_CONTENT + "\"}";
    private static final String BASE_URL = "/api/comment/";

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnStatusOK() throws Exception {
        doGet(new HttpRequestBuilder(BASE_URL)
            .withParameter("isbn", ISBN)
            .build());
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseWhenFoundComments() throws Exception {
        ImmutableMap<String, Object> message = ImmutableMap.of(
            "$.[0].date", COMMENT_DATE.value(),
            "$.[0].author", NAME,
            "$.[0].content", COMMENT_CONTENT,
            "$.[0].isbn", ISBN
        );
        doGet(new HttpRequestBuilder(BASE_URL)
            .withParameter("isbn", ISBN)
            .build(), message);
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseSizeArray() throws Exception {
        doGet(new HttpRequestBuilder(BASE_URL)
            .withParameter("isbn", ISBN)
            .build(), ImmutableMap.of("$.length()", EXPECTED_SIZE));
    }

    @Test
    public void testFetchCommentsShouldReturnExpectedMessageWhenBookNotContainsComments() throws Exception {
        doGet(new HttpRequestBuilder(BASE_URL)
            .withParameter("isbn", ISBN)
            .build(), ImmutableMap.of("$.message", NO_COMMENTS_MESSAGE));
    }

    @Test
    @Sql(value = "classpath:sql_query/comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndReturnStatusOK() throws Exception {
        doPost(new HttpRequestBuilder(BASE_URL + "{userID}/add")
            .withUrlVars(ID)
            .withContent(JSON_REQUEST_WITH_COMMENT)
            .withParameter("bookID", String.valueOf(ID))
            .build());
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldFaildWhenUnableToAddComment() throws Exception {
        doPost(new HttpRequestBuilder(BASE_URL + "{userID}/add")
            .withUrlVars(ID)
            .withContent(JSON_REQUEST_WITH_COMMENT)
            .withParameter("bookID", String.valueOf(ID))
            .withStatus(BAD_REQUEST)
            .build());
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentWithoutCommentFieldsShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON_REQUEST_WITH_COMMENT).delete("$.comment").jsonString();
        addCommentWithExpectedMessageAsResponse(json, "comment field must be not empty");
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentWithTooLongCommentFieldsShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON_REQUEST_WITH_COMMENT).set("$.comment", generateLongComment()).jsonString();
        addCommentWithExpectedMessageAsResponse(json, "comment length must be between 1 and 250");
    }

    private void addCommentWithExpectedMessageAsResponse(String json, String message) throws Exception {
        doPost(new HttpRequestBuilder(BASE_URL + "{userID}/add")
            .withUrlVars(ID)
            .withContent(json)
            .withParameter("bookID", String.valueOf(ID))
            .withStatus(BAD_REQUEST)
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", message));
    }

    private String generateLongComment() {
        return  String.join("", Collections.nCopies(COMMENT_DEFAULT_LENGTH + 1, " "));
    }
}
