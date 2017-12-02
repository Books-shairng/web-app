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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class CommentControllerIT
{
    private static final String NO_COMMENTS_MESSAGE = "Book does not contains any comments";
    private static final String JSON_REQUEST_WITH_COMMENT =
        "{" +
            "\"comment\" : \"" + DomainTestConstants.COMMENT_CONTENT + "\"" +
        "}";
    private static final int EXPECTED_SIZE = 1;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseWhenFoundComments() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[0].date").value(JSONDateConstans.COMMENT_DATE))
            .andExpect(jsonPath("$.[0].author").value(DomainTestConstants.NAME))
            .andExpect(jsonPath("$.[0].content").value(DomainTestConstants.COMMENT_CONTENT))
            .andExpect(jsonPath("$.[0].isbn").value(DomainTestConstants.ISBN));
    }

    @Test
    @Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testFetchCommentsShouldReturnExpectedResponseSizeArray() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.length()").value(EXPECTED_SIZE));
    }

    @Test
    public void testFetchCommentsShouldReturnExpectedMessageWhenBookNotContainsComments() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value(NO_COMMENTS_MESSAGE));
    }

    @Test
    @Sql(value = "classpath:comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndReturnStatusOK() throws Exception {
        mockMvc.perform(post("/api/comment/{userID}/add", DomainTestConstants.ID)
            .param("bookID", String.valueOf(DomainTestConstants.ID))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON_REQUEST_WITH_COMMENT))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldFaildWhenUnableToAddComment() throws Exception {
        mockMvc.perform(post("/api/comment/{userID}/add", DomainTestConstants.ID)
            .param("bookID", String.valueOf(DomainTestConstants.ID))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON_REQUEST_WITH_COMMENT))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }
}
