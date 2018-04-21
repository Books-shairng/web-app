package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;

import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_CONTENT;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.utils.JSONDateConstans.COMMENT_DATE;

import java.util.Collections;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentControllerIT extends AbstractBaseIT
{
    private static final String NO_COMMENTS_MESSAGE = "Book does not contains any comments";
    private static final String JSON_REQUEST_WITH_COMMENT =
        "{" +
            "\"comment\" : \"" + COMMENT_CONTENT + "\"" +
        "}";
    private static final int EXPECTED_SIZE = 1;
    private static final int COMMENT_DEFAULT_LENGTH = 250;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseWhenFoundComments() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[0].date").value(COMMENT_DATE.value()))
            .andExpect(jsonPath("$.[0].author").value(NAME))
            .andExpect(jsonPath("$.[0].content").value(COMMENT_CONTENT))
            .andExpect(jsonPath("$.[0].isbn").value(ISBN));
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseSizeArray() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.length()").value(EXPECTED_SIZE));
    }

    @Test
    public void testFetchCommentsShouldReturnExpectedMessageWhenBookNotContainsComments() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(NO_COMMENTS_MESSAGE));
    }

    @Test
    @Sql(value = "classpath:sql_query/comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndReturnStatusOK() throws Exception {
        mockMvc.perform(post("/api/comment/{userID}/add", ID)
            .param("bookID", String.valueOf(ID))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON_REQUEST_WITH_COMMENT))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldFaildWhenUnableToAddComment() throws Exception {
        mockMvc.perform(post("/api/comment/{userID}/add", ID)
            .param("bookID", String.valueOf(ID))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON_REQUEST_WITH_COMMENT))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
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
        mockMvc.perform(post("/api/comment/{userID}/add", ID)
            .param("bookID", String.valueOf(ID))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(json))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(message))
            .andExpect(status().isBadRequest());
    }

    private String generateLongComment() {
        return  String.join("", Collections.nCopies(COMMENT_DEFAULT_LENGTH + 1, " "));
    }
}
